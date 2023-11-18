package com.teamnine.noFreeRider.notification.dto;

import com.teamnine.noFreeRider.notification.domain.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


public record NotificationDto(
        String notice_title,
        String notice_content
) {

}
