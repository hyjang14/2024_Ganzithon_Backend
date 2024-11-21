package com.ganzithon.Hexfarming.domain.experience.dto.fromServer;

import com.ganzithon.Hexfarming.global.enumeration.Tier;

public record TierListServerDto(Tier tier, int id, String level, int starCount, int requiredExperience) {

    public static TierListServerDto from(Tier tier) {
        return new TierListServerDto(
                tier,
                tier.getId(),
                tier.getLevel(),
                tier.getStarCount(),
                tier.getRequiredExperience()
        );
    }
}
