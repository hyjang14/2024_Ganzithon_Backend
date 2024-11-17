package com.ganzithon.Hexfarming.global.enumeration;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public enum Ability {
    LEADERSHIP(0, "리더십", ""),
    CREATIVITY(1, "창의력", ""),
    COMMUNICATION_SKILL(2, "소통 역량", ""),
    DILIGENCE(3, "성실성", ""),
    RESILIENCE(4, "회복 탄력성", ""),
    TENACITY(5, "인성", "");


    private final int id;
    private final String name;
    private final String description;

    Ability(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static Ability fromId(int id) { // 역량 id로부터 Ability Enum 값을 찾아서 반환
        for (Ability ability : Ability.values()) {
            if (ability.getId() == id) {
                return ability;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 역량 id입니다.");
    }
}
