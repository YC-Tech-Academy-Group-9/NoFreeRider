package com.teamnine.noFreeRider.web.controller;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.dto.CommentDisplayDto;
import com.teamnine.noFreeRider.comments.service.CommentService;
import com.teamnine.noFreeRider.comments.service.RatingInviteService;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.dto.MemberDto;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.NotificationDto;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.GetProjectDto;
import com.teamnine.noFreeRider.project.dto.MemberProjectDto;
import com.teamnine.noFreeRider.project.dto.ProjectDto;
import com.teamnine.noFreeRider.project.service.MemberProjectService;
import com.teamnine.noFreeRider.project.service.ProjectService;
import com.teamnine.noFreeRider.task.dto.TaskDisplayDto;
import com.teamnine.noFreeRider.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ProjectService projectService;
    private final MemberProjectService memberProjectService;
    private final TaskService taskService;
    private final RatingInviteService ratingInviteService;
    private final CommentService commentService;

    @RequestMapping("/")
    public String login(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/main";
        }

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
        NotificationDto[] notificationDtoList = getNotificationListByMember(loginMember);
        model.addAttribute("notificationList", notificationDtoList);

        // projects
        List<Project> projectList = memberProjectService.getProjectListByMember(loginMember);
        GetProjectDto[] projectDtoList = new GetProjectDto[projectList.size()];
        for (int i = 0; i < projectList.size(); i++) {
            int memberCount = memberProjectService.getMemberListByProject(projectList.get(i)).size();
            projectDtoList[i] = new GetProjectDto(
                    projectList.get(i).getId(),
                    projectList.get(i).getProjectName(),
                    projectList.get(i).getProjectSummary(),
                    projectList.get(i).getClassName(),
                    projectList.get(i).getStarted_at(),
                    projectList.get(i).getEnded_at(),
                    projectList.get(i).getStatusCode(),
                    memberCount,
                    projectService.isProjectLeader(loginMember, projectList.get(i))
            );
        }

        // sort by project status and then by project date
        Arrays.sort(projectDtoList, (o1, o2) -> {
            if (o1.statusCode() == o2.statusCode()) {
                return o2.endDate().compareTo(o1.endDate());
            }
            return o1.statusCode().compareTo(o2.statusCode());
        });
        model.addAttribute("projectList", projectDtoList);

        //rating
        UserComment comment = commentService.getCommentByMember(loginMember);
        int count = comment.getNumUpdates();
        if (count == 0) {
            count = 1;
        }
        CommentDisplayDto commentDisplayDto = new CommentDisplayDto(
                (double)comment.getCriteria1() / (double)count,
                (double)comment.getCriteria2() / (double)count,
                (double)comment.getCriteria3() / (double)count,
                (double)comment.getCriteria4() / (double)count);
        model.addAttribute("rating", commentDisplayDto);


        // tasks
        List<TaskDisplayDto> taskList = taskService.searchTasksByMemberId(loginMember.getId());
        model.addAttribute("taskList", taskList);

        return "main";
    }

    @RequestMapping("/project/{projectId}")
    public String projects(Model model, Authentication authentication, @PathVariable UUID projectId) {

        String email = authentication.getName();
        Member loginMember = memberService.getMemberByEmail(email);



        //project info
        Project project = projectService.getProjectInfo(projectId);
        ProjectDto projectDto = new ProjectDto(project.getProjectName(), project.getProjectSummary(), project.getClassName(), project.getStarted_at(), project.getEnded_at(), project.getStatusCode());
        model.addAttribute("project", projectDto);

        // Project members
        List<Member> memberList = memberProjectService.getMemberListByProject(project);

        // Check if the logged-in memberId is part of the project
        boolean isMemberOfProject = memberList.contains(loginMember);

        if (!isMemberOfProject) {
            return "redirect:/main";
        }

        boolean[] isLeaderList = new boolean[memberList.size()];
        MemberDto[] memberDtoList = new MemberDto[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            boolean isLeader = projectService.isProjectLeader(memberList.get(i), project);
            memberDtoList[i] = new MemberDto(
                    memberList.get(i).getId(),
                    memberList.get(i).getMemberName(),
                    memberList.get(i).getMemberEmail(),
                    memberList.get(i).getMemberStudentId(),
                    memberList.get(i).getMemberTemperature());
            isLeaderList[i] = isLeader;
        }

        MemberProjectDto memberProjectDto = new MemberProjectDto(loginMember.getId(), project.getId());

        boolean isLeader = projectService.isProjectLeader(memberProjectDto);
        model.addAttribute("memberList", memberDtoList);
        model.addAttribute("isLeaderList", isLeaderList);
        model.addAttribute("isLeader", isLeader);

        // tasks
        model.addAttribute("taskList", taskService.searchTasksByProjectId(projectId));
        // notifications
        NotificationDto[] notificationDtoList = getNotificationListByMember(loginMember);
        model.addAttribute("notificationList", notificationDtoList);

        return "project";
    }

    @RequestMapping("/rating/{ratingCode}")
    public String rating(Model model, Authentication authentication, @PathVariable UUID ratingCode) {
    	String email = authentication.getName();
        Member loginMember = memberService.getMemberByEmail(email);
        model.addAttribute("name", loginMember.getMemberName());

        boolean isRated = ratingInviteService.isRated(ratingCode);

        UUID projectId = ratingInviteService.getProjectId(ratingCode);

        //check if the memberId is part of the project
        Project project = projectService.getProjectInfo(projectId);
        List<Member> memberList = memberProjectService.getMemberListByProject(project);
        if(!memberList.contains(loginMember) || isRated) {
        	return "redirect:/main";
        }

        //members
        MemberDto[] memberDtoList = new MemberDto[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getId() == loginMember.getId()) {
                continue;
            }
            memberDtoList[i] = new MemberDto(
                    memberList.get(i).getId(),
                    memberList.get(i).getMemberName(),
                    memberList.get(i).getMemberEmail(),
                    memberList.get(i).getMemberStudentId(),
                    memberList.get(i).getMemberTemperature());
        }
        //remove null elements
        memberDtoList = Arrays.stream(memberDtoList)
                .filter(Objects::nonNull)
                .toArray(MemberDto[]::new);
        model.addAttribute("memberList", memberDtoList);

        //notifications
        NotificationDto[] notificationDtoList = getNotificationListByMember(loginMember);
        model.addAttribute("notificationList", notificationDtoList);

        return "rating";
    }

    private NotificationDto[] getNotificationListByMember(Member loginMember) {
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

        return notificationDtoList;
    }

}
