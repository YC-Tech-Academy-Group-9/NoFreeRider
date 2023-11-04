package com.teamnine.noFreeRider.member.domain;

import com.teamnine.noFreeRider.task.domain.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "members_tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "task_id", updatable = false)
    private Task task;
}
