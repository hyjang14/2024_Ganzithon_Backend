package com.ganzithon.Hexfarming.domain.user;

import static org.assertj.core.api.Assertions.*;
import com.ganzithon.Hexfarming.domain.experience.ExperienceService;
import com.ganzithon.Hexfarming.domain.user.dto.fromClient.SignUpClientDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.ResponseTokenServerDto;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetailsService;
import com.ganzithon.Hexfarming.global.utility.JwtManager;
import com.ganzithon.Hexfarming.global.utility.PasswordEncoderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ExperienceService experienceService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private PasswordEncoderManager passwordEncoderManager;
    @Mock
    private JwtManager jwtManager;
    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    public void 유저_초기화() {
        MockitoAnnotations.openMocks(this); // @Mock 초기화
        mockUser = User.builder()
                .email("test@test.com")
                .password("hashedPassword")
                .name("테스트")
                .nickname("테스트닉")
                .build();
    }

    @Test
    public void 회원가입_성공() {
        // given
        SignUpClientDto signUpClientDto = new SignUpClientDto("test@test.com", "password", "password", "테스트", "테스트닉");
        when(passwordEncoderManager.encode(signUpClientDto.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtManager.createToken(mockUser.getId(), false)).thenReturn("accessToken");
        when(jwtManager.createToken(mockUser.getId(), true)).thenReturn("refreshToken");

        // when
        ResponseTokenServerDto result = userService.signUp(signUpClientDto);

        // then
        assertThat(result.accessToken()).isEqualTo("accessToken");
        assertThat(result.refreshToken()).isEqualTo("refreshToken");
    }
}
