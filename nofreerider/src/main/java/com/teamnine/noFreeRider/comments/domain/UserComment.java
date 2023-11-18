package com.teamnine.noFreeRider.comments.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_comments")
@RequiredArgsConstructor
@Getter
public class UserComment {
    // id, user_id, comment, created_at, updated_at
    @Id
    @Column(name = "comment_id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // 평가항목 1 - 기한을 잘 지켜요
    @Column(name="criteria1", nullable = false)
    private int criteria1;

    // 평가항목 2 - 퀄리티가 높아요
    @Column(name="criteria2", nullable = false)
    private int criteria2;

    // 평가항목 3 - 참여도가 높아요
    @Column(name="criteria3", nullable = false)
    private int criteria3;

    // 평가항목 4 - 책임감이 있어요
    @Column(name="criteria4", nullable = false)
    private int criteria4;

//    @Column(name = "comment", nullable = false)
//    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserComment(int userId, int criteria1, int criteria2, int criteria3, int criteria4, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
        this.criteria3 = criteria3;
        this.criteria4 = criteria4;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



}
