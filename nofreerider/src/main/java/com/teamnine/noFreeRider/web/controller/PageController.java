package com.teamnine.noFreeRider.web.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.dto.MemberDto;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.NotificationDto;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.GetProjectDto;
import com.teamnine.noFreeRider.project.dto.ProjectDto;
import com.teamnine.noFreeRider.project.service.MemberProjectService;
import com.teamnine.noFreeRider.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ProjectService projectService;
    private final MemberProjectService memberProjectService;

    @RequestMapping("/")
    public String login() {
        return "home";
    }

    @RequestMapping("/signup")
    public String signup(Model model, Authentication authentication) {

        String email = authentication.getName();
        model.addAttribute("useremail", email);
        return "signup";
    }

    @RequestMapping("/main")
    public String index(Model model, Authentication authentication) {

        String email = authentication.getName();
        Member loginMember = memberService.getMemberByEmail(email);
        model.addAttribute("name", loginMember.getMemberName());
        model.addAttribute("temp", loginMember.getMemberTemperature());

        // notifications
        Notification[] notificationList = notificationService.getNotificationListByMember(loginMember);

        NotificationDto[] notificationDtoList = new NotificationDto[notificationList.length];
        for (int i = 0; i < notificationList.length; i++) {
            String url = "";
            if (notificationList[i].getProject() != null) {
                url = "/projects/" + notificationList[i].getProject().getId();
            }
            notificationDtoList[i] = new NotificationDto(notificationList[i].getId(), notificationList[i].getNoticeTitle(), notificationList[i].getNoticeContent(), notificationList[i].getNoticeUrl());
        }

        model.addAttribute("notificationList", notificationDtoList);

        // projects
        List<Project> projectList = memberProjectService.getProjectListByMember(loginMember);
        GetProjectDto[] projectDtoList = new GetProjectDto[projectList.size()];
        for (int i = 0; i < projectList.size(); i++) {
            projectDtoList[i] = new GetProjectDto(projectList.get(i).getId(), projectList.get(i).getProjectName(), projectList.get(i).getProjectSummary(), projectList.get(i).getClassName(), projectList.get(i).getStarted_at(), projectList.get(i).getEnded_at());
        }
        model.addAttribute("projectList", projectDtoList);

        return "main";
    }

    @RequestMapping("/project/{projectId}")
    public String projects(Model model, Authentication authentication, @PathVariable UUID projectId) {

        //project info
        Project project = projectService.getProjectInfo(projectId);
        ProjectDto projectDto = new ProjectDto(project.getProjectName(), project.getProjectSummary(), project.getClassName(), project.getStarted_at(), project.getEnded_at());
        model.addAttribute("project", projectDto);

        //project members
        Member[] memberList = memberProjectService.getMemberListByProject(project).toArray(new Member[0]);
        MemberDto[] memberDtoList = new MemberDto[memberList.length];
        for (int i = 0; i < memberList.length; i++) {
            memberDtoList[i] = new MemberDto(memberList[i].getMemberName(), memberList[i].getMemberEmail(), memberList[i].getMemberStudentId(), memberList[i].getMemberTemperature());
        }
        model.addAttribute("memberList", memberDtoList);

        return "project";
    }

}
