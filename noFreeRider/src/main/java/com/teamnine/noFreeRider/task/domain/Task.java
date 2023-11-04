package com.teamnine.noFreeRider.task.domain;

import com.teamnine.noFreeRider.project.domain.Project;
import lombok.AccessLevel;
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
    @Column(name = "taskId", updatable = false)
    private UUID taskId;

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "projectId", updatable = false)
    private Project project;

    @Column(name = "taskName", nullable = false)
    private String taskName;

    @Column(name = "taskContend")
    private String taskContend;

    @CreatedDate
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @Column(name = "endedAt")
    private LocalDateTime endedAt;

    @Column(name = "statusCode")
    private byte statusCode;

    public Task(Project project, String taskName, String taskContend, LocalDateTime dueDate) {
        this.project = project;
        this.taskName = taskName;
        this.taskContend = taskContend;
        this.dueDate = dueDate;
        this.statusCode = 0;
    }
}
