package com.ganzithon.Hexfarming.global.enumeration;

public enum ExceptionMessage {
    // global
    INVALID_ABILITY_ID("잘못된 역량 id입니다."),
    INVALID_TIER_ID("잘못된 계급 id입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    CANNOT_CONVERT_FILE("MultipartFIle 변환을 실패하였습니다."),
    CANNOT_UPLOAD_FILE("파일 업로드에 실패하였습니다."),
    CANNOT_DELETE_FILE("파일 삭제에 실패하였습니다."),
    ONLY_IMAGE_FILE_AVAILABLE("이미지 파일만 업로드 가능합니다."),

    // user
    INVALID_USER_ID("잘못된 유저 id입니다."),
    INVALID_USER_INFO("잘못된 유저 정보입니다."),
    INVALID_EMAIL("존재하지 않는 이메일입니다."),
    INVALID_PASSWORD_LENGTH("비밀번호는 8자 이상, 16자 이하여야 합니다."),
    INVALID_EMAIL_OR_PASSWORD("이메일 혹은 비밀번호가 잘못되었습니다."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.");
    ;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
