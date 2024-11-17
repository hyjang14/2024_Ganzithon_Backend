package com.ganzithon.Hexfarming.domain.user.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserValidator {
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static int MAX_PASSWORD_LENGTH = 16;

    public static boolean validateRePasswordIsCorrect(String password, String rePassword) throws IllegalArgumentException {
        if (password.equals(rePassword)) {
            return true;
        }
        return false;
    }

    public static void validatePasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상, 16자 이하여야 합니다.");
        }
    }
}
