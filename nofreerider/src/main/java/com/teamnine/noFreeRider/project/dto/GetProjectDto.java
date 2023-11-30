package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.project.domain.ProjectStatusCode;

import java.util.Date;
import java.util.UUID;

public record GetProjectDto(
        UUID id,
        String name,
        String summary,
        String className,
        Date startDate,
        Date endDate,
        ProjectStatusCode statusCode,
        int memberCount,
        boolean isLeader
) {
}
