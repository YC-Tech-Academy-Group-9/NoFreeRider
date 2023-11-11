package com.teamnine.noFreeRider.project.dto;

import java.util.UUID;

public record AcceptInviteDto(
        UUID projectId,
        UUID invitedCode
) {
}
