package com.ganzithon.Hexfarming.comment;

import com.ganzithon.Hexfarming.post.Post;
import com.ganzithon.Hexfarming.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 pk

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시물

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @Column(nullable = false, length = 50)
    private String writerNickname;

    @Column(nullable = true)
    private String writerTier; // 작성자 티어: 유저에서 가져오는거라 지금은 null상태

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 댓글 작성 시간

    @Column
    private int score; // 점수
}