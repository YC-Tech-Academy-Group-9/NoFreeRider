package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.project.domain.ProjectStatusCode;

import java.util.Date;

public record ProjectDto(
        String name,
        String summary,
        String className,
        Date startDate,
        Date endDate,
        ProjectStatusCode statusCode
) {
}
