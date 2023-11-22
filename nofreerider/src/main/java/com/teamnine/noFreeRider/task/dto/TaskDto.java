package com.teamnine.noFreeRider.task.dto;

import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.domain.TaskStatusCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto (
        String taskName,
        String taskContent,
        LocalDateTime dueDateTime,
        TaskStatusCode status,
        UUID projectId
) {

    public Task toEntity() {
        return Task.builder()
                .task_name(taskName)
                .task_content(taskContent)
                .due_date(dueDateTime)
                .build();
    }
}
