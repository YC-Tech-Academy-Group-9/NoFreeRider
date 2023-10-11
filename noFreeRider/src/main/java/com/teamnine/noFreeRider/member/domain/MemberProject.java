package com.teamnine.noFreeRider.member.domain;

import com.teamnine.noFreeRider.project.domain.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "members_projects")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_no", referencedColumnName = "member_no", updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "project_no", referencedColumnName = "project_no", updatable = false)
    private Project project;

    @Column(name = "score", nullable = true)
    private Integer score;

    public MemberProject(Member member, Project project) {
        this.member = member;
        this.project = project;
        this.score = null;
    }
}
