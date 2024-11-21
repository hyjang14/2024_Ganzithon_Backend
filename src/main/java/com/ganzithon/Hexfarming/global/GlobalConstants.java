package com.ganzithon.Hexfarming.global;

public class GlobalConstants {
    public static String[] noTokenNeededAPIs = {
            "/favicon.ico",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/user/login",
            "/user/signup",
            "/user/checkDuplicateEmail",
            "/user/checkDuplicateNickname",
            "/user/checkRePassword",
            "/experience/getAbilityList",
            "/experience/getTierList",
    };

    public static String S3URL = "https://hexfarming.s3.ap-northeast-2.amazonaws.com/";
}
