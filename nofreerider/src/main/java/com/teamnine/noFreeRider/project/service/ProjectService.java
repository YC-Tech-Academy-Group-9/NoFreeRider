package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.domain.MemberProject;
import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.domain.ProjectStatusCode;
import com.teamnine.noFreeRider.project.dto.*;
import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

    public Project save(AddProjectDto addProjectDto) {
        Project project = projectRepository.save(addProjectDto.toEntity());
        memberProjectRepository.save(new MemberProject(addProjectDto.leader(), project));
        return project;
    }

    @Transactional
    public Project changeLeader(ChangeProjectLeaderDto dto, UUID projectID) throws Exception {
        Project project = projectRepository.findById(projectID)
                .orElseThrow(IllegalArgumentException::new);

        if (!project.getLeader().getId().equals(dto.exLeader_id())) {
            throw new IllegalArgumentException();
        }

        Member nLeader = memberRepository.findById(dto.newLeader_id())
                .orElseThrow(IllegalArgumentException::new);

        project.updateLeaderNo(nLeader);

        return project;
    }

    public Project addMember(MemberProjectDto dto) {
        Project project = projectRepository.findById(dto.project_id())
                .orElseThrow(IllegalArgumentException::new);

        if (memberProjectRepository.existsByMemberIdAndProjectId(dto.member_id(), dto.project_id())) {
            throw new IllegalArgumentException();
        }

        memberProjectRepository.save(MemberProject.builder()
                .member(memberRepository.findById(dto.member_id())
                        .orElseThrow(IllegalArgumentException::new))
                .project(project)
                .build());

        return project;
    }

    @Transactional
    public Project changeStatusCode(UUID projectId, StatusCodeDto dto) {
        Project project = projectRepository.findById(projectId).orElseThrow(IllegalArgumentException::new);
        project.updateStatusCode(dto.statusCode());
        return project;
    }

    public boolean isProjectLeader(MemberProjectDto dto) {
        Project project = projectRepository.findById(dto.project_id()).orElse(null);
        if (project == null) {
            return false;
        }
        UUID leaderUUID = project.getLeader().getId();
        return leaderUUID.equals(dto.member_id());
    }

    public boolean isProjectLeader(Member member, Project project) {
        if (project == null) {
            return false;
        }
        UUID leaderUUID = project.getLeader().getId();
        return leaderUUID.equals(member.getId());
    }

    @Transactional
    public Project update(UpdateProjectDto dto) {
        Project project = projectRepository.findById(dto.project_id())
                .orElseThrow(IllegalArgumentException::new);
        project.updateNameAndSummary(dto);
        return project;
    }

    public boolean isExistProject(UUID projectId) {
        Optional<Project> projectBox = projectRepository.findById(projectId);
        return !projectBox.isEmpty();
    }

    public Project getProjectInfo(UUID projectId) {
        Optional<Project> projectBox = projectRepository.findById(projectId);
        return projectBox.get();
    }
}
