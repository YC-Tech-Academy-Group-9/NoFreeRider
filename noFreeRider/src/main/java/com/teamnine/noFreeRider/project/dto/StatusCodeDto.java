package com.teamnine.noFreeRider.project.dto;

public record StatusCodeDto(
        int code
) {
    public StatusCodeDto(int code) {
        if (code < 0 || code > 3) {
            throw new IllegalArgumentException("잘못된 코드 입니다.");
        }
        this.code = code;
    }
}
