package com.ganzithon.Hexfarming.domain.comment.dto.fromClient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long postId;
    private String content;
    private int score;
}

