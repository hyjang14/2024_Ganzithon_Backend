package com.ganzithon.Hexfarming.global.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component // 스프링 Bean에서 관리
public class PasswordEncoderManager {
    private BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderManager() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encode(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    // 사용자가 입력한 패스워드(inputPassword)가 진짜 DB에 저장된 패스워드(realPassword)가 일치하는지 확인
    public boolean matches(String inputPassword, String realPassword) {
        return this.passwordEncoder.matches(inputPassword, realPassword);
    }
}
