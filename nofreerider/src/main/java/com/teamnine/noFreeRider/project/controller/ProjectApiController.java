package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberDetailService;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.dto.ContentDto;
import com.teamnine.noFreeRider.notification.dto.PostInviteDto;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.*;
import com.teamnine.noFreeRider.project.service.InviteService;
import com.teamnine.noFreeRider.project.service.MemberProjectService;
import com.teamnine.noFreeRider.project.service.ProjectService;
import com.teamnine.noFreeRider.task.service.TaskService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectApiController {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final MemberDetailService memberDetailService;
    private final MemberProjectService memberProjectService;
    private final TaskService taskService;
    private final InviteService inviteService;
    private final NotificationService notificationService;

    @PostMapping("")
    public ResponseEntity<ResultDto<Project>> addProject(
            @RequestBody ProjectDto projectDto,
            Principal principal
            ) {
        try {
            String userName = principal.getName();
            Member leader = memberDetailService.loadUserByUsername(userName);
            AddProjectDto addProjectDto = new AddProjectDto(
                    projectDto.name(),
                    projectDto.summary(),
                    projectDto.className(),
                    projectDto.startDate(),
                    projectDto.endDate(),
                    leader);

            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            projectService.save(addProjectDto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            400,
                            "Failed to add project",
                            null
                    ));
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ResultDto<Project>> projectInfo(
            @PathVariable UUID projectId
    ) {
        if (!isExistProject(projectId)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            404,
                            "not found project",
                            null
                    ));
        }
        return ResponseEntity.ok()
                .body(new ResultDto<>(
                        200,
                        "",
                        projectService.getProjectInfo(projectId)
                ));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ResultDto<Project>> updateProject(
            @PathVariable UUID projectId,
            @RequestBody ProjectDto dto,
            Principal principal
            ) {
        if (!isExistProject(projectId)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            404,
                            "not found project",
                            null
                    ));
        }
        if (!isProjectLeader(principal.getName(), projectId)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            403,
                            "access only project leader",
                            null
                    ));
        }
        return ResponseEntity.ok()
                .body(new ResultDto<>(
                        200,
                        "",
                        projectService.update(new UpdateProjectDto(projectId, dto.name(), dto.summary(), dto.className()))
                ));

    }

    @PutMapping("/{projectId}/leader/{newLeaderId}")
    public ResponseEntity<ResultDto<Project>> updateProjectLeader(
            @PathVariable UUID projectId,
            @PathVariable UUID newLeaderId,
            Principal principal
            ) {
        try {
            if (!isProjectLeader(principal.getName(), projectId)) {
                return ResponseEntity.badRequest()
                        .body(new ResultDto<>(
                                403,
                                "access only project leader",
                                null
                        ));
            }

            Project updateLeaderProject = projectService.changeLeader(newLeaderId, projectId);
            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            updateLeaderProject
                    ));
        } catch (Exception e) {
            if (e.getMessage().equals("not found project")) {
                return ResponseEntity.status(406)
                        .body(new ResultDto<>(
                                406,
                                e.getMessage(),
                                null
                        ));
            }
            if (e.getMessage().equals("already leader")) {
                return ResponseEntity.status(409)
                        .body(new ResultDto<>(
                                409,
                                e.getMessage(),
                                null
                        ));
            }
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            400,
                            e.getMessage(),
                            null
                    ));
        }

    }

    @PutMapping("/{projectId}/status")
    public ResponseEntity<ResultDto<Project>> updateStatusCode(
            @PathVariable UUID projectId,
            @RequestBody StatusCodeDto dto,
            Principal principal
    ) {
        try {
            if (!isProjectLeader(principal.getName(), projectId)) {
                return ResponseEntity.badRequest()
                        .body(new ResultDto<>(
                                403,
                                "access only project leader",
                                null
                        ));
            }

            Project updateStatusProject = projectService.changeStatusCode(projectId, dto);
            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            updateStatusProject
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            400,
                            "Bad Request",
                            null
                    ));
        }
    }

    @PostMapping("/{projectId}/invite")
    public ResponseEntity<ResultDto<ContentDto>> generateInvite(
            @PathVariable UUID projectId,
            @RequestBody CreateInviteDto dto,
            Principal principal
    ) {
        if (!isProjectLeader(principal.getName(), projectId)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            403,
                            "access only project leader",
                            null
                    ));
        }
        try {
            UUID inviteCode = inviteService.create(projectId);
            Member member = memberService.getMemberByEmail(dto.memberEmail());

            boolean isAlreadyMember = memberProjectService.isMemberPartInProject(new MemberProjectDto(member.getId(), projectId));
            if (isAlreadyMember) {
                return ResponseEntity.status(409)
                        .body(new ResultDto<ContentDto>(
                                409,
                                "already member",
                                null
                        ));
            }

            PostInviteDto postInviteDto = new PostInviteDto(projectId, inviteCode, member.getId());

            return ResponseEntity.ok()
                    .body(new ResultDto<ContentDto>(
                            200,
                            "",
                            notificationService.postInviteMessage(postInviteDto)
                    ));
        } catch (Exception e) {
            if (e.getMessage().equals("not found member")) {
                return ResponseEntity.status(404)
                        .body(new ResultDto<ContentDto>(
                                404,
                                e.getMessage(),
                                null
                        ));
            }
            return ResponseEntity.status(401)
                    .body(new ResultDto<ContentDto>(
                            401,
                            e.getMessage(),
                            null
                    ));
        }
    }
    @PostMapping("/{projectId}/invite/{inviteCode}")
    public ResponseEntity<ResultDto<Project>> addPartyMember(
            @PathVariable UUID projectId,
            @PathVariable UUID inviteCode,
            Principal principal
    ) {
        try {
            MemberProjectDto dto = new MemberProjectDto(
                    getMemberUUID(principal.getName()),
                    inviteService.useCode(new AcceptInviteDto(projectId, inviteCode)));

            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            projectService.addMember(dto)
                    ));
        } catch (Exception e) { // 세분화 추가 작업 필요
           return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            400,
                            e.getMessage(),
                            null
                    ));
        }
    }

    @DeleteMapping("/{projectId}/member/{memberId}")
    public ResponseEntity<ResultDto<Long>> deletePartyMember(
            @PathVariable UUID projectId,
            @PathVariable UUID memberId,
            Principal principal
    ) {
        if (!isProjectLeader(principal.getName(), projectId)) {
            return ResponseEntity.status(403)
                    .body(new ResultDto<>(
                            403,
                            "access only project leader",
                            null
                    ));
        }

        Member memberToRemove = memberService.getMemberById(memberId);

        MemberProjectDto dto = new MemberProjectDto(memberToRemove.getId(), projectId);

        if (!memberProjectService.isMemberPartInProject(dto)) {
            return ResponseEntity.status(404)
                    .body(new ResultDto<>(
                            404,
                            "member not in the project",
                            null
                    ));
        }

        return ResponseEntity.ok()
                .body(new ResultDto<>(
                        200,
                        "",
                        memberProjectService.deleteMemberProject(dto)
                ));
    }

    private UUID getMemberUUID(String userName) {
        Member member = memberDetailService.loadUserByUsername(userName);
        return member.getId();
    }

    private boolean isProjectLeader(String userName, UUID projectId) {
        return projectService.isProjectLeader(new MemberProjectDto(getMemberUUID(userName), projectId));
    }

    private boolean isExistProject(UUID projectId) {
        return projectService.isExistProject(projectId);
    }
}
