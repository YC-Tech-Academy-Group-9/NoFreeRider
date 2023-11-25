package com.teamnine.noFreeRider.notification.dto;

import com.teamnine.noFreeRider.notification.domain.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


public record NotificationDto(
        UUID notice_id,
        String notice_title,
        String notice_content,
        String notice_url
) {

}
