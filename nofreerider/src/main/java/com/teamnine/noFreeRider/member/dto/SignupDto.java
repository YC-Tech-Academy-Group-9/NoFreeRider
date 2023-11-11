package com.teamnine.noFreeRider.member.dto;

public record SignupDto (
        String userName, String realName, String email, int studentId, String major
) {
}
