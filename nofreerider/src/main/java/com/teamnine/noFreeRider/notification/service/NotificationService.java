package com.teamnine.noFreeRider.notification.service;

import com.teamnine.noFreeRider.comments.service.RatingInviteService;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberProject;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.ContentDto;
import com.teamnine.noFreeRider.notification.dto.PostInviteDto;
import com.teamnine.noFreeRider.notification.repository.NotificationRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final RatingInviteService ratingInviteService;;

    public ContentDto postInviteMessage(PostInviteDto dto) {
        Project project = projectService.getProjectInfo(dto.projectID());
        Member member = memberService.getMemberById(dto.destinationMemberID());
        String inviteUrl = "/projects/" + dto.projectID() + "/invite/" + dto.inviteCode();
        String title = project.getProjectName() + " 초대";
        String content = String.format("%s에서 초대 메시지를 보냈습니다!", project.getProjectName());

        Notification notification = Notification.builder()
                .project(project)
                .member(member)
                .noticeTitle(title)
                .noticeContent(content)
                .noticeUrl(inviteUrl)
                .build();

        notificationRepository.save(notification);
        return new ContentDto(notification.getNoticeTitle(), notification.getNoticeContent(), dto.destinationMemberID());
    }

    public Notification[] getNotificationListByMember(Member member) {
        List<Notification> notificationList = notificationRepository.findAllByMember(member);
        Notification[] notificationArray = new Notification[notificationList.size()];
        for (int i = 0; i < notificationList.size(); i++) {
            notificationArray[i] = notificationList.get(i);
        }
        return notificationArray;
    }

    @Transactional
    public UUID deleteNotification(UUID notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        if (!notification.getMember().getMemberEmail().equals(username)) {
            throw new IllegalArgumentException("unauthorized delete request");
        }
        try {
            notificationRepository.deleteById(notificationId);
            return notificationId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Notification not found");
        }
    }

    public void sendReviewMessage(List<MemberProject> members, UUID projectId) {
        if (members.size() <= 1) {
            return;
        }
        Member[] memberArr = members.stream()
                .map(MemberProject::getMember)
                .toArray(Member[]::new);
        Project project = members.get(0)
                .getProject();
        String title = String.format("%s 동료 평가", project.getProjectName());
        String content = String.format("%s 프로젝트가 완료되었습니다. 동료 평가를 실시해주세요!", project.getProjectName());

        for (Member member : memberArr) {
            UUID ratingCode = ratingInviteService.create(projectId);
            String reviewUrl = "/comments/rating/" + ratingCode;
            Notification notification = Notification.builder()
                    .project(project)
                    .member(member)
                    .noticeTitle(title)
                    .noticeContent(content)
                    .noticeUrl(reviewUrl)
                    .build();
            notificationRepository.save(notification);
        }
    }
}
