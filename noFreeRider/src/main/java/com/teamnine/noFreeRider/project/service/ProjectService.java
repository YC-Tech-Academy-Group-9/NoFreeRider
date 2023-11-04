package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.domain.MemberProject;
import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.project.domain.Project;
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

    public Project save(AddProjectDto addProjectDto) {
        return projectRepository.save(addProjectDto.toEntity());
    }

    @Transactional
    public Project changeLeader(ChangeProjectLeaderDto dto, UUID projectID) throws Exception {
        Project project = projectRepository.findById(projectID)
                .orElseThrow(() -> new IllegalArgumentException());

        if (!project.getLeader().getMemberNo().equals(dto.exLeaderId())) {
            throw new IllegalArgumentException();
        }

        Member nLeader = memberRepository.findById(dto.newLeaderId())
                .orElseThrow(() -> new IllegalArgumentException());

        project.updateLeaderNo(nLeader);

        return project;
    }

    public Project addMember(MemberProjectDto dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new IllegalArgumentException());

        if (memberProjectRepository.existsByMember_noAndProject_no(dto.memberId(), dto.projectId())) {
            throw new IllegalArgumentException();
        }

        memberProjectRepository.save(MemberProject.builder()
                .member(memberRepository.findById(dto.memberId())
                        .orElseThrow(() -> new IllegalArgumentException()))
                .project(project)
                .build());

        return project;
    }


    public boolean isProjectLeader(MemberProjectDto dto) {
        Optional<UUID> leaderUUID = projectRepository.findLeader_noByProjectId(dto.projectId());
        if (leaderUUID.isEmpty()) {
            return false;
        }
        if (leaderUUID.get().equals(dto.memberId())) {
            return true;
        }
        return false;
    }

    public Project update(UpdateProjectDto dto) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new IllegalArgumentException());
        project.updateNameAndSummary(dto);
        return project;
    }
}
