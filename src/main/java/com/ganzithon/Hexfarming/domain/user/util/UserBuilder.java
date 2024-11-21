package com.ganzithon.Hexfarming.domain.user.util;

import com.ganzithon.Hexfarming.domain.user.User;

public class UserBuilder {
    public static User build(String email, String hashedPassword, String name, String nickname) {
        return User.builder()
                .email(email)
                .password(hashedPassword)
                .name(name)
                .nickname(nickname)
                .build();
    }
}
