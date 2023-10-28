package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.AddProjectDto;
import com.teamnine.noFreeRider.project.dto.ChangeProjectLeaderDto;
import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Project save(AddProjectDto addProjectDto) {
        return projectRepository.save(addProjectDto.toEntity());
    }

    @Transactional
    public Project changeLeader(ChangeProjectLeaderDto dto, UUID projectID) throws Exception {
        Project project = projectRepository.findById(projectID)
                .orElseThrow(() -> new IllegalArgumentException());

        if (!project.getLeader().getMember_no().equals(dto.exLeaderID())) {
            throw new IllegalArgumentException();
        }

        Member nLeader = memberRepository.findById(dto.newLeaderID())
                .orElseThrow(() -> new IllegalArgumentException());

        project.updateLeader_no(nLeader);

        return project;
    }

}
