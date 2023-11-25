package com.teamnine.noFreeRider.project.dto;

import java.util.Date;
import java.util.UUID;

public record GetProjectDto(
        UUID id,
        String name,
        String summary,
        String className,
        Date startDate,
        Date endDate,
        int memberCount
) {
}
