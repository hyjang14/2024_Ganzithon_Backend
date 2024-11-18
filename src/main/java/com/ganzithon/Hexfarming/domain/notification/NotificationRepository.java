package com.ganzithon.Hexfarming.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Optional<List<Notification>> findAllByUserId(int userId);
    Optional<Integer> countByUserId(int userId);
}
