package com.teamnine.noFreeRider.member.dto;

public record MemberDisplayDto(
        String name,
        String email,
        int studentId,
        String major,
        short temp,
        double criteria1,
        double criteria2,
        double criteria3,
        double criteria4
) {
}
