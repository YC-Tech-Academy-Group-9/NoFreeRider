package com.teamnine.noFreeRider.task.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.service.ProjectService;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.domain.TaskStatusCode;
import com.teamnine.noFreeRider.task.dto.TaskCreateDto;
import com.teamnine.noFreeRider.task.dto.TaskDto;
import com.teamnine.noFreeRider.task.service.TaskService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskAPIController {
    private final TaskService taskService;
    private final MemberService memberService;
    private final ProjectService projectService;
    @PostMapping("/create")
    public ResponseEntity<ResultDto<TaskDto>> createTask(@RequestBody TaskCreateDto taskDto) {
        try {
            Task newTask = taskService.createTask(taskDto);
            TaskDto newTaskDto = new TaskDto(
                    newTask.getTaskName(),
                    newTask.getTaskContent(),
                    newTask.getDue_date(),
                    newTask.getStatus_code(),
                    newTask.getProject().getId()
            );
            return ResponseEntity.ok().body(
                    new ResultDto<>(
                            200,
                            "success",
                            newTaskDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultDto<>(
                            400,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @PostMapping("/update/{taskId}")
    public ResponseEntity<ResultDto<TaskDto>> updateTask(@PathVariable UUID taskId, @RequestBody TaskDto taskDto) {
        try {
            Task updatedTask = taskService.getTaskById(taskId);
            TaskDto updatedTaskDto = new TaskDto(
                    updatedTask.getTaskName(),
                    updatedTask.getTaskContent(),
                    updatedTask.getDue_date(),
                    updatedTask.getStatus_code(),
                    updatedTask.getProject().getId()
            );
            return ResponseEntity.ok().body(
                    new ResultDto<TaskDto>(
                            200,
                            "success",
                            updatedTaskDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultDto<TaskDto>(
                            400,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @PostMapping("/status/{taskId}/{status_code}")
    public ResponseEntity<ResultDto<TaskDto>> completeTask(@PathVariable UUID taskId, @PathVariable TaskStatusCode status_code) {
        try {
            Task targetTask = taskService.getTaskById(taskId);
            TaskDto updatedTaskDto = new TaskDto(
                    targetTask.getTaskName(),
                    targetTask.getTaskContent(),
                    targetTask.getDue_date(),
                    status_code,
                    targetTask.getProject().getId()
            );
            Task updatedTask = taskService.updateTask(taskId, updatedTaskDto);
            TaskDto newTaskDto = new TaskDto(
                    updatedTask.getTaskName(),
                    updatedTask.getTaskContent(),
                    updatedTask.getDue_date(),
                    updatedTask.getStatus_code(),
                    updatedTask.getProject().getId()
            );
            return ResponseEntity.ok().body(
                    new ResultDto<>(
                            200,
                            "success",
                            newTaskDto
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResultDto<>(
                            400,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @PostMapping("/delete/{taskId}")
    public ResponseEntity<ResultDto<TaskDto>> deleteTask(@PathVariable UUID taskId, Principal principal) {
        Member member = memberService.getMemberByEmail(principal.getName());
        Project project = taskService.getProjectByTaskId(taskId);
        // if currentUser deleting task is a memberId of Project
        if(projectService.isProjectLeader(member, project)) {
            try {
                taskService.deleteTask(taskId);

                return ResponseEntity.ok().body(
                        new ResultDto<>(
                                200,
                                "success",
                                null
                        ));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(
                        new ResultDto<>(
                                400,
                                e.getMessage(),
                                null
                        ));
            }
        } // else reject deletion and send 403 error
        else {
            return ResponseEntity.badRequest().body(
                    new ResultDto<>(
                            403,
                            "Unauthorized task-deletion",
                            null
                    ));
        }
    }


}
