package com.ganzithon.Hexfarming.domain.comment.dto.fromServer;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private String content;
    private String writerNickname;
    private String writerUsername;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    private LocalDateTime createdAt;
    private int score;


    public CommentResponseDto(Long id, String content, String writerNickname, LocalDateTime createdAt, int score) {
        this.id = id;
        this.content = content;
        this.writerNickname = writerNickname;
        this.writerUsername = writerUsername;
        this.createdAt = createdAt;
        this.score = score;
    }


    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getScore() {
        return score;
    }
}

