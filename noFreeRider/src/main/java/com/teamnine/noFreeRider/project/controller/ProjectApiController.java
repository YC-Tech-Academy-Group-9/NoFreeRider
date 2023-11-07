package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberDetailService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.*;
import com.teamnine.noFreeRider.project.service.MemberProjectService;
import com.teamnine.noFreeRider.project.service.ProjectService;
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
    private final MemberDetailService memberDetailService;
    private final MemberProjectService memberProjectService;

    @PostMapping("")
    public ResponseEntity<ResultDto<Project>> addProject(
            @RequestBody ProjectDto projectDto,
            Principal principal
    ) {
        String userName = principal.getName();
        Member leader = memberDetailService.loadUserByUsername(userName);
        AddProjectDto addProjectDto = new AddProjectDto(
                projectDto.name(),
                projectDto.summary(),
                projectDto.className(),
                leader);

        return ResponseEntity.ok()
                .body(new ResultDto<>(
                        200,
                        "",
                        projectService.save(addProjectDto)));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ResultDto<Project>> updateProject(
            @PathVariable UUID projectId,
            @RequestBody ProjectDto dto,
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

        return ResponseEntity.ok()
                .body(new ResultDto<>(
                        200,
                        "",
                        projectService.update(new UpdateProjectDto(projectId, dto.name(), dto.summary(), dto.className()))
                ));

    }

    @PutMapping("/{project_id}/leader")
    public ResponseEntity<ResultDto<Project>> updateProjectLeader(
            @PathVariable UUID project_id,
            @RequestBody ChangeProjectLeaderDto dto,
            Principal principal
    ) {
        try {
            if (!isProjectLeader(principal.getName(), project_id)) {
                return ResponseEntity.badRequest()
                        .body(new ResultDto<>(
                                403,
                                "access only project leader",
                                null
                        ));
            }

            Project updateLeaderProject = projectService.changeLeader(dto, project_id);
            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            updateLeaderProject
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

    @PutMapping("/{project_id}/status")
    public ResponseEntity<ResultDto<Project>> updateStatusCode(
            @PathVariable UUID project_id,
            @RequestBody StatusCodeDto dto,
            Principal principal
    ) {
        try {
            if (!isProjectLeader(principal.getName(), project_id)) {
                return ResponseEntity.badRequest()
                        .body(new ResultDto<>(
                                403,
                                "access only project leader",
                                null
                        ));
            }

            Project updateStatusProject = projectService.changeStatusCode(project_id, dto);
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


    @PostMapping("/{project_id}")

    public ResponseEntity<ResultDto<Project>> addPartyMember(
            @PathVariable UUID project_id,
            Principal principal
    ) {
        try {
            MemberProjectDto dto = new MemberProjectDto(getMemberUUID(principal.getName()), project_id);
            return ResponseEntity.ok()
                    .body(new ResultDto<>(
                            200,
                            "",
                            projectService.addMember(dto)
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

    @DeleteMapping("/{project_id}/{member_id}")
    public ResponseEntity<ResultDto<Long>> deletePartyMember(
            @PathVariable UUID project_id,
            @PathVariable UUID member_id,
            Principal principal
    ) {
        if (!isProjectLeader(principal.getName(), project_id)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            403,
                            "access only project leader",
                            null
                    ));
        }

        MemberProjectDto dto = new MemberProjectDto(member_id, project_id);

        if (!memberProjectService.isMemberPartInProject(dto)) {
            return ResponseEntity.badRequest()
                    .body(new ResultDto<>(
                            400,
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
}
