package com.teamnine.noFreeRider.comments.dto;

import com.teamnine.noFreeRider.member.domain.Member;

import java.util.UUID;

public record ReceiveCommentDto(
        UUID member,
        int criteria1,
        int criteria2,
        int criteria3,
        int criteria4
) {
}
