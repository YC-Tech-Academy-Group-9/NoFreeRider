package com.teamnine.noFreeRider.Project.domain;

import com.teamnine.noFreeRider.Member.domain.Member;
import lombok.AccessLevel;
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
    @Column(name = "project_no", updatable = false)
    private UUID project_no;

    @Column(name = "project_name")
    private String project_name;

    @Column(name = "status_code")
    private byte status_code; // 0 : 시작, 1 : 진행, 2 : 중단, 3: 완료

    @ManyToOne
    @JoinColumn(name = "leader_no", referencedColumnName = "member_no", updatable = false)
    private Member member;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "ended_at")
    private LocalDateTime ended_at;

    public Project(String project_name, Member member) {
        this.project_name = project_name;
        this.status_code = 0;
        this.member = member;
    }
}
