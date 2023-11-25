package com.teamnine.noFreeRider.task.dto;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.domain.TaskStatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskDisplayDto(
        UUID taskId,
        String taskName,
        String taskContent,
        LocalDateTime dueDateTime,
        TaskStatusCode taskStatusCode,
        List<String> memberName,
        UUID projectId
) {
}
