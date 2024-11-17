package com.ganzithon.Hexfarming.domain.experience.dto.fromServer;

import com.ganzithon.Hexfarming.global.enumeration.Ability;

public record AbilityListServerDto(Ability ability, int id, String name, String description) {

    public static AbilityListServerDto from(Ability ability) {
        return new AbilityListServerDto(
                ability,
                ability.getId(),
                ability.getName(),
                ability.getDescription()
        );
    }
}
