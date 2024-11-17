package com.ganzithon.Hexfarming.domain.post.dto.fromServer;

public class AverageScoreResponseDto {
    private int averageScore;
    private Long postId;

    public AverageScoreResponseDto(Long postId, int averageScore) {
        this.postId = postId;
        this.averageScore = averageScore;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}

