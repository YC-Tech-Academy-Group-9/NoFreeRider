package com.teamnine.noFreeRider.task.service;

import com.teamnine.noFreeRider.project.dto.SearchTaskListDto;
import com.teamnine.noFreeRider.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public SearchTaskListDto searchTasksByProjectId(UUID projectId) {
        return new SearchTaskListDto(taskRepository.findAllByProject_id(projectId));
    }
}
