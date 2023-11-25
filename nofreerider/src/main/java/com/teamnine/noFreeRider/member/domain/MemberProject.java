package com.teamnine.noFreeRider.member.domain;

import com.teamnine.noFreeRider.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "membersProjects")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "memberId", updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId", updatable = false)
    private Project project;

    @Builder
    public MemberProject(Member member, Project project) {
        this.member = member;
        this.project = project;
    }
}
