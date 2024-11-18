package com.ganzithon.Hexfarming.domain.notification;

import com.ganzithon.Hexfarming.domain.post.Post;
import com.ganzithon.Hexfarming.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    private boolean isCheckPoints;

    @Column(length = 1000)
    private String message;
}
