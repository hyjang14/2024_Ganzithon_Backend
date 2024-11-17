package com.ganzithon.Hexfarming.domain.experience.dto.fromServer;

import com.ganzithon.Hexfarming.domain.experience.Experience;
import com.ganzithon.Hexfarming.global.enumeration.Ability;

public record ExperienceServerDto(Ability ability, TierServerDto tier, int experience) {
    public static ExperienceServerDto from(Experience experience) {
        return new ExperienceServerDto(
                experience.getAbility(),
                new TierServerDto(experience.getTier()),
                experience.getExperience()
        );
    }
}
