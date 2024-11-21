package com.ganzithon.Hexfarming.domain.user;

import com.ganzithon.Hexfarming.domain.experience.ExperienceService;
import com.ganzithon.Hexfarming.domain.user.dto.fromClient.*;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckPasswordServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.ResponseTokenServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckDuplicateServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.UserInformationServerDto;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetails;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetailsService;
import com.ganzithon.Hexfarming.domain.user.util.UserBuilder;
import com.ganzithon.Hexfarming.domain.user.util.UserValidator;
import com.ganzithon.Hexfarming.global.enumeration.ExceptionMessage;
import com.ganzithon.Hexfarming.global.utility.JwtManager;
import com.ganzithon.Hexfarming.global.utility.PasswordEncoderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ExperienceService experienceService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoderManager passwordEncoderManager;
    private final JwtManager jwtManager;

    @Autowired
    public UserService(UserRepository userRepository, ExperienceService experienceService, CustomUserDetailsService customUserDetailsService, PasswordEncoderManager passwordEncoderManager, JwtManager jwtManager) {
        this.userRepository = userRepository;
        this.experienceService = experienceService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoderManager = passwordEncoderManager;
        this.jwtManager = jwtManager;
    }

    @Transactional
    public ResponseTokenServerDto signUp(SignUpClientDto dto) throws IllegalArgumentException {
        UserValidator.validatePasswordLength(dto.password());
        String hashedPassword = passwordEncoderManager.encode(dto.password());

        User newUser = UserBuilder.build(dto.email(), hashedPassword, dto.name(), dto.nickname());
        userRepository.save(newUser);

        experienceService.initiateAbilityExperience(newUser);

        String accessToken = jwtManager.createToken(newUser.getId(), false);
        String refreshToken = jwtManager.createToken(newUser.getId(), true);

        return new ResponseTokenServerDto(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public ResponseTokenServerDto logIn(LoginClientDto dto) {
        String email = dto.email();
        String password = dto.password();

        User existUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_EMAIL_OR_PASSWORD.getMessage()));

        if (passwordEncoderManager.matches(password, existUser.getPassword())) {
            String accessToken = jwtManager.createToken(existUser.getId(), false);
            String refreshToken = jwtManager.createToken(existUser.getId(), true);

            return new ResponseTokenServerDto(accessToken, refreshToken);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_EMAIL_OR_PASSWORD.getMessage());
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
    public UserInformationServerDto myInformation() {
        CustomUserDetails nowUser = customUserDetailsService.getCurrentUserDetails();
        return userInformation(nowUser.getUser().getId());
    }

    @Transactional(readOnly = true)
    public UserInformationServerDto userInformation(int userId) {
        User theUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_USER_ID.getMessage()));
        return new UserInformationServerDto(theUser.getEmail(), theUser.getName(), theUser.getNickname());
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionMessage.INVALID_PASSWORD.getMessage());
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
