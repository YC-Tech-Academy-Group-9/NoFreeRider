package com.teamnine.noFreeRider.notification.repository;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Optional<List<Notification>> findAllByMember(Member member);
}
