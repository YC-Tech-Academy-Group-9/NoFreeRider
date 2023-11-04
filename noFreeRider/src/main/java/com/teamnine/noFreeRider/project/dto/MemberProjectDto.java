package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record MemberProjectDto(
        UUID memberId,
        UUID projectId
) {
}
