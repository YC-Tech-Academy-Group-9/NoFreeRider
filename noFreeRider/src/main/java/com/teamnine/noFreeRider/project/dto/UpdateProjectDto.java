package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record UpdateProjectDto(
        UUID project_id,
        String title,
        String summary,
        String className) {
    public UpdateProjectDto(UUID project_id, String title, String summary, String className) {
        if (title.isEmpty()) {
            throw new IllegalArgumentException("프로젝트 제목을 입력 바랍니다.");
        }
        this.project_id = project_id;
        this.title = title;
        this.summary = summary;
        this.className = className;
    }
}
