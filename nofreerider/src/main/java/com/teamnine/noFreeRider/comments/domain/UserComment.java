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
    private int userId;

    @Column(name = "rating", nullable = false)
    private short rating;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserComment(int userId, String comment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



}
