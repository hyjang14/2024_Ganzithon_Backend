package com.ganzithon.Hexfarming.domain.experience.util;

import com.ganzithon.Hexfarming.domain.experience.Experience;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import com.ganzithon.Hexfarming.global.enumeration.Tier;

public class ExperienceCreator {
    public static Experience create(User user, Ability ability, Tier tier) {
        return Experience.builder()
                .user(user)
                .ability(ability)
                .tier(tier)
                .experience(0)
                .build();
    }
}
