package com.teamnine.noFreeRider.task.dto;

import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.domain.TaskStatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskCreateDto(
        String taskName,
        String taskContent,
        LocalDateTime dueDateTime,
        UUID projectId,
        List<Integer> memberIds
) {

}
