package com.teamnine.noFreeRider.project.domain;

import com.teamnine.noFreeRider.Member.domain.Member;
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
    @Column(name = "projectId", updatable = false)
    private UUID projectId;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "projectSummary")
    private String projectSummary;

    @Column(name = "statusCode")
    private byte statusCode; // 0 : 시작, 1 : 진행, 2 : 중단, 3: 완료

    @ManyToOne
    @JoinColumn(name = "leaderNo", referencedColumnName = "memberNo", updatable = false)
    private Member leader;

    @CreatedDate
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "endedAt")
    private LocalDateTime endedAt;

    @Builder
    public Project(String projectName, String projectSummary,Member leader) {
        this.projectName = projectName;
        this.projectSummary = projectSummary;
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
}
