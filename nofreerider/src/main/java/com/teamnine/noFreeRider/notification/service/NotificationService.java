package com.teamnine.noFreeRider.notification.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.ContentDto;
import com.teamnine.noFreeRider.notification.dto.PostInviteDto;
import com.teamnine.noFreeRider.notification.repository.NotificationRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectService projectService;
    private final MemberService memberService;

    private final String rootUrl = "https://localhost:8080";

    public ContentDto postInviteMessage(PostInviteDto dto) {
        Project project = projectService.getProjectInfo(dto.projectID());
        Member member = memberService.getMemberById(dto.destinationMemberID());
        String inviteUrl = rootUrl + "/projects/" + dto.projectID() + "/invite/" + dto.inviteCode();
        String title = project.getProjectName() + "에 초대합니다.";
        String content = String.format("%s에서 초대 메시지를 보냈습니다.\n%s", project.getProjectName(), inviteUrl);

        Notification notification = Notification.builder()
                .project(project)
                .member(member)
                .notice_title(title)
                .notice_content(content)
                .build();

        notificationRepository.save(notification);
        return new ContentDto(notification.getNotice_title(), notification.getNotice_content(), dto.destinationMemberID());
    }

}
