package com.teamnine.noFreeRider.project.domain;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.dto.UpdateProjectDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "projects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status_code")
    private ProjectStatusCode statusCode;

    @OneToOne
    @JoinColumn(name = "leader_id", updatable = false)
    private Member leader;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "ended_at")
    private LocalDateTime ended_at;

    @Builder
    public Project(String project_name, String project_summary, Member leader) {
        this.projectName = project_name;
        this.projectSummary = project_summary;
        this.statusCode = ProjectStatusCode.STARTED;
        this.leader = leader;
    }

    public void updateLeaderNo(Member nLeader) {
        this.leader = nLeader;
    }
    public void updateNameAndSummary(UpdateProjectDto dto) {
        this.projectName = dto.name();
        this.projectSummary = dto.summary();
    }

    public void updateStatusCode(ProjectStatusCode newCode) {
        this.statusCode = newCode;
    }
}
