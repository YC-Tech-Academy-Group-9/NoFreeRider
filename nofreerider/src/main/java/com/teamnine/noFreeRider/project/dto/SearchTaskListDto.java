package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.task.domain.Task;

import java.util.List;

public record SearchTaskListDto(
        List<Task> tasks
) {

}
