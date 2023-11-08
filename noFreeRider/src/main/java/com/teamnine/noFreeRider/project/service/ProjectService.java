package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.domain.MemberProject;
import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.domain.ProjectStatusCode;
import com.teamnine.noFreeRider.project.dto.*;
import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Transactional
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

        if (memberProjectRepository.existsByMember_idAndProject_id(dto.member_id(), dto.project_id())) {
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
    public Project changeStatusCode(UUID project_id, StatusCodeDto dto) {
        Optional<Project> projectBox = projectRepository.findById(project_id);
        if (projectBox.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트 입니다");
        }
        Project project = projectBox.get();
        if (project.getStatusCode() == dto.code()) {
            throw new IllegalArgumentException();
        }
        if (dto.code().equals(ProjectStatusCode.DONE)) {
            project.setEnded_atToNow();
        }
        project.updateStatusCode(dto.code());
        return project;
    }

    public boolean isProjectLeader(MemberProjectDto dto) {
        Optional<UUID> leaderUUID = projectRepository.findLeader_idById(dto.project_id());
        if (leaderUUID.isEmpty()) {
            return false;
        }
        return leaderUUID.get().equals(dto.member_id());
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
        if (projectBox.isEmpty()) {
            return false;
        }
        return true;
    }
}
