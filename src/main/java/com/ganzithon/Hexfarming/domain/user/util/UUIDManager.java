package com.ganzithon.Hexfarming.domain.user.util;

import java.util.UUID;

public class UUIDManager {
    public static String generateUniqueString() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
