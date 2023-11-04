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
    private UUID projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_summary")
    private String projectSummary;

    @Column(name = "status_code")
    private int statusCode; // 0 : 시작, 1 : 진행, 2 : 중단, 3: 완료

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
        this.statusCode = 0;
        this.leader = leader;
    }

    public void updateLeaderNo(Member nLeader) {
        this.leader = nLeader;
    }
    public void updateNameAndSummary(UpdateProjectDto dto) {
        this.projectName = dto.name();
        this.projectSummary = dto.summary();
    }

    public void updateStatusCode(int newCode) {
        this.statusCode = newCode;
    }
}
