package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record ChangeProjectLeaderDto(
        UUID exLeaderId,
        UUID newLeaderId
) {
}
