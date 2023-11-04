package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record MemberProjectDto(
        UUID member_id,
        UUID project_id
) {
}
