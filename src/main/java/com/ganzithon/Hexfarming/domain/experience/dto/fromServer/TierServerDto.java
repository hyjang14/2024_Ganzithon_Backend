package com.ganzithon.Hexfarming.domain.experience.dto.fromServer;

import com.ganzithon.Hexfarming.global.enumeration.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TierServerDto {
    private final String tierLevel;
    private final int starCount;
    private final int experienceToTierUp;

    public TierServerDto(Tier tier) {
        this.tierLevel = tier.getLevel();
        this.starCount = tier.getStarCount();
        this.experienceToTierUp = tier.getRequiredExperience();
    }
}
