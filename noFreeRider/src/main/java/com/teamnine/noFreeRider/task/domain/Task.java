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
    @Column(name = "task_no", updatable = false)
    private UUID task_no;

    @ManyToOne
    @JoinColumn(name = "project_no", referencedColumnName = "project_no", updatable = false)
    private Project project;

    @Column(name = "task_name", nullable = false)
    private String task_name;

    @Column(name = "task_contend")
    private String task_contend;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "due_date")
    private LocalDateTime due_date;

    @Column(name = "ended_at")
    private LocalDateTime ended_at;

    @Column(name = "status_code")
    private byte status_code;

    public Task(Project project, String task_name, String task_contend, LocalDateTime due_date) {
        this.project = project;
        this.task_name = task_name;
        this.task_contend = task_contend;
        this.due_date = due_date;
        this.status_code = 0;
    }
}
