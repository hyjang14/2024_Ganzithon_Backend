package com.ganzithon.Hexfarming.dto.fromServer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ganzithon.Hexfarming.post.Category;
import com.ganzithon.Hexfarming.post.Post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String categoryName;
    private String remainingTime; // 남은 시간
    private int view; // 조회수
    private String writerNickname;
    private String tier;
    private String imageBase64; // Base64로 인코딩된 이미지 데이터
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


    public PostResponseDto(
            Long postId,
            String title,
            String content,
            String categoryName,
            String remainingTime,
            int view,
            String writerNickname,
            String tier,
            String imageBase64,
            LocalDateTime createdAt
    ) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.remainingTime = remainingTime;
        this.view = view;
        this.writerNickname = writerNickname;
        this.tier = tier;
        this.imageBase64 = imageBase64;
        this.createdAt = createdAt;
    }


    public static PostResponseDto fromEntity(Post post) {
        // 남은 시간 계산
        long remainingSeconds = Duration.between(LocalDateTime.now(), post.getTimer()).getSeconds();

        String formattedRemainingTime = formatSecondsToHHMMSS(Math.max(remainingSeconds, 0));

        // Base64로 이미지 인코딩
        String imageBase64 = null;
        if (post.getImage() != null) {
            imageBase64 = Base64.getEncoder().encodeToString(post.getImage());
        }

        return new PostResponseDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getDisplayName(),
                formattedRemainingTime,
                post.getView(),
                post.getWriter().getNickname(),
                post.getWriter().getTier(),
                imageBase64,
                post.getCreatedAt()
        );
    }


    private static String formatSecondsToHHMMSS(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }


    public Long getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategoryName() {
        return categoryName; // 수정
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getWriterNickname() {
        return writerNickname;
    }

    public void setWriterNickname(String writerNickname) {
        this.writerNickname = writerNickname;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}