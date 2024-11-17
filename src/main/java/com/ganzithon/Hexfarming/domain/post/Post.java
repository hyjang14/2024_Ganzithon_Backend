package com.ganzithon.Hexfarming.domain.post;

import com.ganzithon.Hexfarming.domain.comment.Comment;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 100, nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Column(length = 50, nullable = false)
    private String writerNickname;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 5000, nullable = false)
    private String content;

    @Column(nullable = false)
    private int view = 0; // 게시물 조회수 초기값 0

    @Column(nullable = false)
    private LocalDateTime timer;

    private boolean isTimerOver;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ability ability; // 게시물 카테고리 (Enum으로 6개 중 하나 선택하는 형태)

    @Lob
    private byte[] image;

    private int scoreSum = 0; // 점수 합산값

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}
