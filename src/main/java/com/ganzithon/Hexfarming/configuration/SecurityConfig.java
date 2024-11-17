package com.ganzithon.Hexfarming.configuration;

import com.ganzithon.Hexfarming.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 파일이라는 것을 알리는 애너테이션
@EnableWebSecurity // WebSecurity를 허용 ?
public class SecurityConfig {
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter; // JwtAuthentication이 Bean에 등록되어 있으므로 자동으로 주입됨
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf를 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // basic 인가 방식 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기존 로그인 폼 비활성화 (로그인 로직을 알아서 만듦)

                // stateless 세션 유지
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // URL 별로 권한을 설정
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(
                                    "/favicon.ico",
                                    "/user/login",
                                    "/user/signup"
                            ).permitAll() // 위 Endpoint에 해당하는 요청은 토큰 없이도 접근 가능 + JwtAuthenticationFilter.java에서도 추가해 줘야 함
                            .anyRequest().authenticated(); // 그 외 나머지 요청들은 JWT 토큰이 있어야 접근 가능
                })

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
