package com.teamnine.noFreeRider.domain;

import lombok.AccessLevel;
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
    @Column(name = "notice_no", updatable = false)
    private UUID notice_no;

    @ManyToOne
    @JoinColumn(name = "project_no", referencedColumnName = "project_no", updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "notifier_no", referencedColumnName = "member_no", updatable = false)
    private Member member;

    @Column(name = "notice_title", nullable = false)
    private String notice_title;

    @Column(name = "notice_contend", nullable = false)
    private String notice_contend;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;
}
