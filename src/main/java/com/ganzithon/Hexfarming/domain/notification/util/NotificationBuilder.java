package com.ganzithon.Hexfarming.domain.notification.util;

import com.ganzithon.Hexfarming.domain.notification.Notification;
import com.ganzithon.Hexfarming.domain.post.Post;
import com.ganzithon.Hexfarming.domain.user.User;

public class NotificationBuilder {
    public static Notification build(Post post, User user, boolean isCheckPoints, String message) {
        return Notification.builder()
                .post(post)
                .user(user)
                .isCheckPoints(isCheckPoints)
                .message(message)
                .build();
    }
}
