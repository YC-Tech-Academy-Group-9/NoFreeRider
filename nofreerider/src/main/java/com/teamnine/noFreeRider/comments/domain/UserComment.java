package com.teamnine.noFreeRider.comments.domain;


import com.teamnine.noFreeRider.comments.dto.UpdateCommentDto;
import com.teamnine.noFreeRider.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_comments")
@RequiredArgsConstructor
@Getter
public class UserComment {
    // id, user_id, comment, created_at, updated_at
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "comment_id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "member_id", updatable = false)
    private Member member;

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

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Used to calculate the average of the criteria
    @Column(name = "num_updats", nullable = false)
    private int numUpdates;

    @Builder
    public UserComment(
            Member member,
            int criteria1,
            int criteria2,
            int criteria3,
            int criteria4,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int numUpdates
    ) {
        this.member = member;
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
        this.criteria3 = criteria3;
        this.criteria4 = criteria4;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.numUpdates = numUpdates;
    }

    public void updateCommentCriteria(UpdateCommentDto updateCommentDto) {
        int incNumUpdates = this.numUpdates + 1;
        this.criteria1 = (updateCommentDto.criteria1() + this.criteria1 * this.numUpdates) / incNumUpdates;
        this.criteria2 = (updateCommentDto.criteria2() + this.criteria2 * this.numUpdates) / incNumUpdates;
        this.criteria3 = (updateCommentDto.criteria3() + this.criteria3 * this.numUpdates) / incNumUpdates;
        this.criteria4 = (updateCommentDto.criteria4() + this.criteria4 * this.numUpdates) / incNumUpdates;
        this.updatedAt = LocalDateTime.now();
        this.numUpdates = incNumUpdates;
    }

}
