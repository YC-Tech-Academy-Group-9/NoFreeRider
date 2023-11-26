package com.teamnine.noFreeRider.comments.dto;

import java.util.List;

public record CommentFormDto(
        List<ReceiveCommentDto> comments
) {
}
