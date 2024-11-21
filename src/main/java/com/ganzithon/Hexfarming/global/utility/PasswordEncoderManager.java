package com.ganzithon.Hexfarming.global.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderManager {
    private BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderManager() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encode(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String inputPassword, String realPassword) {
        return this.passwordEncoder.matches(inputPassword, realPassword);
    }
}
