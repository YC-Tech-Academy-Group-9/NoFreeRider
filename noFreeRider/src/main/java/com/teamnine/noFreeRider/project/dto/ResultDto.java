package com.teamnine.noFreeRider.project.dto;

public record ResultDto<T>(
        int code,
        String message,
        T data
) {
}
