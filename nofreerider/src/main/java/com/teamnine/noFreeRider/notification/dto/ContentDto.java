package com.teamnine.noFreeRider.notification.dto;

import java.util.UUID;

public record ContentDto(
        String title,
        String content,
        UUID destinationMember
) {
}
