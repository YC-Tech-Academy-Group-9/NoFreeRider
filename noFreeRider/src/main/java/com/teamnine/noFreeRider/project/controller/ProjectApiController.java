package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.Member.domain.Member;
import com.teamnine.noFreeRider.Member.service.MemberDetailService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.AddProjectDto;
import com.teamnine.noFreeRider.project.dto.ChangeProjectLeaderDto;
import com.teamnine.noFreeRider.project.dto.ProjectDto;
import com.teamnine.noFreeRider.project.dto.ResultDto;
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
                        projectService.update(new UpdateProjectDto(projectId, dto.name(), dto.summary()))
                ));

    }

    @PutMapping("/{projectId}/leader")
    public ResponseEntity<ResultDto<Project>> updateProjectLeader(
            @PathVariable UUID projectId,
            @RequestBody ChangeProjectLeaderDto dto
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

            Project updateLeaderProject = projectService.changeLeader(dto, projectId);
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

    @PostMapping("/{projectId}")
    public ResponseEntity<ResultDto<Project>> addPartiMember(
            @PathVariable UUID projectId,
            Principal principal
    ) {
        try {
            MemberProjectDto dto = new MemberProjectDto(getMemberUUID(principal.getName()), projectId);
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

    @DeleteMapping("/{projectId}/{memberId}")
    public ResponseEntity<ResultDto<Long>> deletePartiMember(
            @PathVariable UUID projectId,
            @PathVariable UUID memberId,
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

        MemberProjectDto dto = new MemberProjectDto(memberId, projectId);

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
        return member.getMemberNo();
    }

    private boolean isProjectLeader(String userName, UUID projectId) {
        return projectService.isProjectLeader(new MemberProjectDto(getMemberUUID(userName), projectId));
    }
}
