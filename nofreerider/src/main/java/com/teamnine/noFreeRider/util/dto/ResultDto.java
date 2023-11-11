package com.teamnine.noFreeRider.util.dto;

public record ResultDto<T>(
        int code,
        String message,
        T data
) {
}
