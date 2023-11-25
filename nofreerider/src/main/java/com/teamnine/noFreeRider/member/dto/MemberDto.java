package com.teamnine.noFreeRider.member.dto;

import java.util.UUID;

public record MemberDto (
        UUID id,
        String name,
        String email,
        int studentId,
        short temp
){
}
