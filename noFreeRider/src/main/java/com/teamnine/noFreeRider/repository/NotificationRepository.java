package com.teamnine.noFreeRider.repository;

import com.teamnine.noFreeRider.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
