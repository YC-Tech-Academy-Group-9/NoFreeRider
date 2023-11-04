package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record UpdateProjectDto(
        UUID project_id,
        String name,
        String summary) {
    public UpdateProjectDto(UUID project_id, String name, String summary) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.project_id = project_id;
        this.name = name;
        this.summary = summary;
    }
}
