package com.teamnine.noFreeRider.comments.dto;

import java.util.UUID;

public record ReceiveCommentDto(
        UUID memberId,
        int criteria1,
        int criteria2,
        int criteria3,
        int criteria4
) {
}
