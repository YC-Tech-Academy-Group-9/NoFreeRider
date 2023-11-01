package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record UpdateProjectDto(
        UUID projectId,
        String name,
        String summary) {
    public UpdateProjectDto(UUID projectId, String name, String summary) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.projectId = projectId;
        this.name = name;
        this.summary = summary;
    }
}
