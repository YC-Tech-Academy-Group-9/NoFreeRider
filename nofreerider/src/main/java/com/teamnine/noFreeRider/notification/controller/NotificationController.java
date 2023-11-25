package com.teamnine.noFreeRider.notification.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.NotificationDto;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberService memberService;
    @PostMapping("/delete/{notificationId}")
    public ResponseEntity<ResultDto<UUID>> deleteNotification(
            @PathVariable UUID notificationId,
            Principal principal) {
        if (notificationId == null) {
            return ResponseEntity.badRequest().body(
                    new ResultDto<UUID>(
                            403,
                            "notificationId is null",
                            null));
        }

        try {
            UUID deletedId = notificationService.deleteNotification(notificationId, principal.getName());
            return ResponseEntity.ok()
                    .body(new ResultDto<UUID>(
                    200,
                    "success",
                            deletedId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<UUID>(
                    400,
                    "notification not found",
                    null));
        }




    }
}
