package com.teamnine.noFreeRider.project.domain;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.dto.UpdateProjectDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Table(name = "projects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Project {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "project_id", updatable = false)
    private UUID id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_summary")
    private String projectSummary;

    @Column(name = "class_name")
    private String className;

    @Column(name = "status_code")
    private ProjectStatusCode statusCode;

    @OneToOne
    @JoinColumn(name = "leader_id", updatable = false)
    private Member leader;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "started_at")
    private Date started_at;

    @Column(name = "ended_at")
    private Date ended_at;

    @Builder
    public Project(String project_name, String project_summary, String className, Date startDate, Date endDate, Member leader) {
        this.projectName = project_name;
        this.projectSummary = project_summary;
        this.className = className;
        this.statusCode = ProjectStatusCode.STARTED;
        this.started_at = startDate;
        this.ended_at = endDate;
        this.leader = leader;
    }

    public void updateLeaderNo(Member nLeader) {
        this.leader = nLeader;
    }

    public void updateNameAndSummary(UpdateProjectDto dto) {
        this.projectName = dto.name();
        this.projectSummary = dto.summary();
        this.className = dto.className();
    }

    public void updateStatusCode(ProjectStatusCode newCode) {
        this.statusCode = newCode;
    }
}
