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
    private String noticeTitle;

    @Column(name = "notice_content", nullable = false)
    private String noticeContent;

    @Column(name = "notice_url")
    private String noticeUrl;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Notification(Project project, Member member, String noticeTitle, String noticeContent, String noticeUrl) {
        this.project = project;
        this.member = member;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeUrl = noticeUrl;
        this.createdAt = LocalDateTime.now();
    }
}
