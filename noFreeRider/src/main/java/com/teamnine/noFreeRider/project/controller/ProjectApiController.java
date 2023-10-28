package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberDetailService;
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

    @PutMapping("/{id}")
    public ResponseEntity<ResultDto<Project>> updateProjectLeader(
            @PathVariable UUID id,
            @RequestBody ChangeProjectLeaderDto dto
            ) {
        try {
            Project updateLeaderProject = projectService.changeLeader(dto, id);
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
}
