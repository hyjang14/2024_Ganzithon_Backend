package com.ganzithon.Hexfarming.domain.notification;

import com.ganzithon.Hexfarming.domain.notification.dto.fromClient.DeleteNotificationClientDto;
import com.ganzithon.Hexfarming.domain.notification.dto.fromServer.ResponseNotificationCountServerDto;
import com.ganzithon.Hexfarming.domain.notification.dto.fromServer.ResponseNotificationServerDto;
import com.ganzithon.Hexfarming.domain.post.Post;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.domain.user.UserService;
import com.ganzithon.Hexfarming.domain.user.util.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final String checkPointMessage = "\"%s\"을 쓴지 24시간이 지났어요. 점수를 확인해 볼까요?";
    private final String checkFeedback = "\"%s\"에 \"%s\" 님이 피드백을 주셨어요. 한 번 확인해 볼까요?";

    private final NotificationRepository notificationRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, CustomUserDetailsService customUserDetailsService) {
        this.notificationRepository = notificationRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional(readOnly = true)
    public ResponseNotificationCountServerDto myNotificationCount() {
        int nowUserId = customUserDetailsService.getCurrentUserDetails().getUser().getId();
        Optional<Integer> notificationCount = notificationRepository.countByUserId(nowUserId);
        return notificationCount.isPresent() ? new ResponseNotificationCountServerDto(notificationCount.get()) : new ResponseNotificationCountServerDto(0);
    }

    @Transactional(readOnly = true)
    public List<ResponseNotificationServerDto> getMyNotifications() {
        int nowUserId = customUserDetailsService.getCurrentUserDetails().getUser().getId();
        return getNotifications(nowUserId);
    }

    @Transactional(readOnly = true)
    public List<ResponseNotificationServerDto> getNotifications(int userId) {
        Optional<List<Notification>> notificationsOptional = notificationRepository.findAllByUserId(userId);
        if (notificationsOptional.isEmpty()) {
            return new ArrayList<>();
        }

        return notificationsOptional.get().stream()
                .map(ResponseNotificationServerDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(DeleteNotificationClientDto dto) {
        notificationRepository.deleteById(dto.notificationId());
    }

    @Transactional
    public void saveCheckPoints(Post post) {
        Notification newNotification = Notification.builder()
                .post(post)
                .user(post.getWriter())
                .isCheckPoints(true)
                .message(String.format(checkPointMessage, post.getTitle()))
                .build();
        notificationRepository.save(newNotification);
    }

    @Transactional
    public void saveCheckFeedBack(Post post, User user) {
        Notification newNotification = Notification.builder()
                .post(post)
                .user(post.getWriter())
                .isCheckPoints(false)
                .message(String.format(checkFeedback, post.getTitle(), user.getNickname()))
                .build();
        notificationRepository.save(newNotification);
    }
}
