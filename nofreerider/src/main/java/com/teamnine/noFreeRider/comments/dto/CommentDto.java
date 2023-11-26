package com.teamnine.noFreeRider.comments.dto;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.member.domain.Member;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public record CommentDto (
        Member member,
        int criteria1,
        int criteria2,
        int criteria3,
        int criteria4,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int numUpdates
) {

    public CommentDto(UserComment comment) {
        this(
                comment.getMember(),
                comment.getCriteria1(),
                comment.getCriteria2(),
                comment.getCriteria3(),
                comment.getCriteria4(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getNumUpdates()
        );
    }

    public UserComment toEntity() {
        return UserComment.builder()
                .member(member)
                .criteria1(criteria1)
                .criteria2(criteria2)
                .criteria3(criteria3)
                .criteria4(criteria4)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .numUpdates(numUpdates)
                .build();
    }
}
