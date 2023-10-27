package com.teamnine.noFreeRider.project.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberDetailService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.AddProjectDto;
import com.teamnine.noFreeRider.project.dto.ProjectDto;
import com.teamnine.noFreeRider.project.dto.ResultDto;
import com.teamnine.noFreeRider.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectApiController {

    private final ProjectService projectService;
    private final MemberDetailService memberDetailService;

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
}
