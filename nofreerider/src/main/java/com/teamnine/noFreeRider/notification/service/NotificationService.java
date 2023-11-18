package com.teamnine.noFreeRider.notification.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.repository.NotificationRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberService memberService;

    public void createNotification(Project targetProject, Member notifier, String title, String content) {

    }

    public Notification[] getNotificationListByMember(Member member) {

        List<Notification> notificationList = notificationRepository.findAllByMember(member).orElseThrow();
        return notificationList.toArray(new Notification[0]);
    }

}
