package com.teamnine.noFreeRider.Notification.repository;

import com.teamnine.noFreeRider.Notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
