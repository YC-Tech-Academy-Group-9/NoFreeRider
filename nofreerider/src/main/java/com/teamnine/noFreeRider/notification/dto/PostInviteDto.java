package com.teamnine.noFreeRider.notification.dto;

import java.util.UUID;

public record PostInviteDto(
        UUID projectID,
        UUID inviteCode,
        UUID destinationMemberID
) {
}
