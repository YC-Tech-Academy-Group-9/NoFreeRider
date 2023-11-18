package com.teamnine.noFreeRider.notification.domain;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "notice_id", updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "notifier_id", updatable = false)
    private Member member;

    @Column(name = "notice_title", nullable = false)
    private String notice_title;

    @Column(name = "notice_content", nullable = false)
    private String notice_content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Builder
    public Notification(Project project, Member member, String notice_title, String notice_content) {
        this.project = project;
        this.member = member;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
    }
}
