package com.teamnine.noFreeRider.comments.dto;

import java.util.UUID;

public record ReceiveCommentDto(
        UUID memberId,
        int criteria1,
        int criteria2,
        int criteria3,
        int criteria4
) {
    public int getTemperatureDiff() {
        return this.criteria1 + this.criteria2 + this.criteria3 + this.criteria4 - 12;
    }
}
