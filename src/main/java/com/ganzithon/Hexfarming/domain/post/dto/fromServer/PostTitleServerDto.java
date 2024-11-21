package com.ganzithon.Hexfarming.domain.post.dto.fromServer;

import com.ganzithon.Hexfarming.global.enumeration.Ability;

public record PostTitleServerDto(long id, Ability ability, String title) {}
