package com.ganzithon.Hexfarming.global.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public enum Tier {
    TIER1_1(0, "Tier1 ★", 50),
    TIER1_2(1, "Tier1 ★★", 60),
    TIER1_3(2, "Tier1 ★★★", 72),
    TIER1_4(3, "Tier1 ★★★★", 86),
    TIER1_5(4, "Tier1 ★★★★★", 104),

    TIER2_1(5, "Tier2 ★", 156),
    TIER2_2(6, "Tier2 ★★", 187),
    TIER2_3(7,"Tier2 ★★★", 224),
    TIER2_4(8, "Tier2 ★★★★", 269),
    TIER2_5(9, "Tier2 ★★★★★", 322),

    TIER3_1(10, "Tier3 ★", 484),
    TIER3_2(11, "Tier3 ★★", 580),
    TIER3_3(12, "Tier3 ★★★", 697),
    TIER3_4(13, "Tier3 ★★★★", 836),
    TIER3_5(14, "Tier3 ★★★★★", 1003),

    TIER4_1(15, "Tier4 ★", 1505),
    TIER4_2(16, "Tier4 ★★", 1806),
    TIER4_3(17, "Tier4 ★★★", 2167),
    TIER4_4(18, "Tier4 ★★★★", 2600),
    TIER4_5(19, "Tier4 ★★★★★", 3120),

    TIER5_1(20, "Tier5 ★", 4680),
    TIER5_2(21, "Tier5 ★★", 5616),
    TIER5_3(22, "Tier5 ★★★", 6739),
    TIER5_4(23, "Tier5 ★★★★", 8087),
    TIER5_5(24, "Tier5 ★★★★★", 9704);


    private final int id;
    private final String name;
    private final int requiredExperience;

    Tier(int id, String name, int requiredExperience) {
        this.id = id;
        this.name = name;
        this.requiredExperience = requiredExperience;
    }

    public static Tier fromId(int id) { // 계급 id로부터 Tier Enum 값을 찾아서 반환
        for (Tier tier : Tier.values()) {
            if (tier.getId() == id) {
                return tier;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 계급 id입니다.");
    }

    public static Tier caculateTier(int experience) {
        for (Tier tier : Tier.values()) {
            if (experience < tier.getRequiredExperience()) {
                return tier;
            }
        }
        return Tier.TIER5_5;
    }
}
