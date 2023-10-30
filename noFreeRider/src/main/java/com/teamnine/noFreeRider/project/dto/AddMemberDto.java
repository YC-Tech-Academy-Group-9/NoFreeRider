package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record AddMemberDto(
        UUID memberId,
        UUID projectId
) {
}
