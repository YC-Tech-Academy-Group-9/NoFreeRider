package com.teamnine.noFreeRider.task.domain;

import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.task.dto.TaskDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tasks")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "task_id", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
    private Project project;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "task_content")
    private String taskContent;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "due_date")
    private LocalDateTime due_date;

    @Column(name = "status_code")
    private TaskStatusCode status_code;

    @Builder
    public Task(Project project, String task_name, String task_content, LocalDateTime due_date) {
        this.project = project;
        this.taskName = task_name;
        this.taskContent = task_content;
        this.due_date = due_date;
        this.created_at = LocalDateTime.now();
        this.status_code = TaskStatusCode.TODO;
    }

    @Builder
    public Task(Project project, String task_name, String task_content, LocalDateTime due_date, TaskStatusCode status_code) {
        this.project = project;
        this.taskName = task_name;
        this.taskContent = task_content;
        this.due_date = due_date;
        this.created_at = LocalDateTime.now();
        this.status_code = status_code;
    }

    public void updateTask(TaskDto taskDto) {
        this.taskName = taskDto.taskName();
        this.taskContent = taskDto.taskContent();
        this.due_date = taskDto.dueDateTime();
        this.status_code = taskDto.status();
    }
}
