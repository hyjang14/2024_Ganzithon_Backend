package com.ganzithon.Hexfarming.post;

public enum Category {
    CATEGORY1("리더쉽"),
    CATEGORY2("창의력"),
    CATEGORY3("커뮤니케이션 역량"),
    CATEGORY4("성실성"),
    CATEGORY5("회복 탄력성"),
    CATEGORY6("인성");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
