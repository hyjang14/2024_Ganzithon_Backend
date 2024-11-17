package com.ganzithon.Hexfarming.domain.experience.dto.fromServer;

import com.ganzithon.Hexfarming.global.enumeration.Tier;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TierServerDto {
    private final String tierName;
    private final int experienceToTierUp;

    public TierServerDto(Tier tier) {
        this.tierName = tier.getName();
        this.experienceToTierUp = tier.getRequiredExperience();
    }
}
