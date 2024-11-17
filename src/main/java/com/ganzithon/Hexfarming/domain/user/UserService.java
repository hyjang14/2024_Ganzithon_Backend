package com.ganzithon.Hexfarming.domain.user;

import com.ganzithon.Hexfarming.domain.experience.ExperienceService;
import com.ganzithon.Hexfarming.domain.user.dto.fromClient.*;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckPasswordServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.ResponseTokenServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckDuplicateServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.UserInformationServerDto;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetails;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetailsService;
import com.ganzithon.Hexfarming.domain.user.util.UserValidator;
import com.ganzithon.Hexfarming.global.utility.JwtManager;
import com.ganzithon.Hexfarming.global.utility.PasswordEncoderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service // Service 클래스(로직을 처리)임을 알려줌
public class UserService {
    private final UserRepository userRepository;
    private final ExperienceService experienceService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoderManager passwordEncoderManager;
    private final JwtManager jwtManager;

    @Autowired // Bean으로 관리하고 있는 객체들을 자동으로 주입해줌
    public UserService(UserRepository userRepository, ExperienceService experienceService, CustomUserDetailsService customUserDetailsService, PasswordEncoderManager passwordEncoderManager, JwtManager jwtManager) {
        this.userRepository = userRepository;
        this.experienceService = experienceService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoderManager = passwordEncoderManager;
        this.jwtManager = jwtManager;
    }

    @Transactional // DB에 접근한다는 것을 알리는 애너테이션
    public ResponseTokenServerDto signUp(SignUpClientDto dto) throws IllegalArgumentException {
        // 패스워드의 길이를 검사
        UserValidator.validatePasswordLength(dto.password());

        // 비밀번호 암호화(해싱)
        String hashedPassword = passwordEncoderManager.encode(dto.password());

        // 새로운 유저를 생성하여 DB에 저장
        User newUser = User.builder()
                .email(dto.email())
                .password(hashedPassword)
                .nickname(dto.nickname())
                .build();
        userRepository.save(newUser);
        experienceService.initiateAbilityExperience(newUser); // 역량 별 경험치 0으로 초기화

        // Jwt 토큰 생성 후 발급
        String accessToken = jwtManager.createToken(newUser.getId(), false);
        String refreshToken = jwtManager.createToken(newUser.getId(), true);

        return new ResponseTokenServerDto(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public ResponseTokenServerDto logIn(LoginClientDto dto) {
        String email = dto.email();
        String password = dto.password();

        // 해당 email으로 등록된 유저가 있는지 확인하고 없으면 예외
        User existUser = userRepository.findByEmail(email);
        if (existUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다.");
        }

        // 패스워드가 일치하는지 확인 후 토큰 반환
        if (passwordEncoderManager.matches(password, existUser.getPassword())) {
            String accessToken = jwtManager.createToken(existUser.getId(), false);
            String refreshToken = jwtManager.createToken(existUser.getId(), true);

            return new ResponseTokenServerDto(accessToken, refreshToken);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 잘못되었습니다.");
    }

    @Transactional(readOnly = true)
    public CheckDuplicateServerDto checkDuplicateEmail(CheckDuplicateEmailClientDto dto) {
        boolean result = userRepository.existsByEmail(dto.email());
        return new CheckDuplicateServerDto(result);
    }

    @Transactional(readOnly = true)
    public CheckDuplicateServerDto checkDuplicateNickname(CheckDuplicateNicknameClientDto dto) {
        boolean result = userRepository.existsByNickname(dto.nickname());
        return new CheckDuplicateServerDto(result);
    }

    @Transactional(readOnly = true)
    public CheckPasswordServerDto checkRePassword(CheckRePasswordClientDto dto) {
        boolean result = UserValidator.validateRePasswordIsCorrect(dto.password(), dto.rePassword());
        return new CheckPasswordServerDto(result);
    }

    @Transactional(readOnly = true)
    public UserInformationServerDto userInformation() {
        CustomUserDetails nowUser = customUserDetailsService.getCurrentUserDetails();
        return new UserInformationServerDto(nowUser.getUsername(), nowUser.getNickname());
    }

    @Transactional(readOnly = true)
    public CheckPasswordServerDto checkPassword(CheckPasswordClientDto dto) {
        CustomUserDetails nowUser = customUserDetailsService.getCurrentUserDetails();
        boolean result = passwordEncoderManager.matches(dto.password(), nowUser.getPassword());
        return new CheckPasswordServerDto(result);
    }

    @Transactional
    public void changePassword(ChangePasswordClientDto dto) {
        if (!UserValidator.validateRePasswordIsCorrect(dto.password(), dto.rePassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
        UserValidator.validatePasswordLength(dto.password());

        User nowUser = customUserDetailsService.getCurrentUserDetails().getUser();
        nowUser.setPassword(dto.password());
    }

    @Transactional
    public void changeNickname(ChangeNicknameClientDto dto) {
        User nowUser = customUserDetailsService.getCurrentUserDetails().getUser();
        nowUser.setNickname(dto.nickname());
    }

    @Transactional
    public void withdraw() {
        User nowUser = customUserDetailsService.getCurrentUserDetails().getUser();
        userRepository.delete(nowUser);
        SecurityContextHolder.clearContext();
    }
}
