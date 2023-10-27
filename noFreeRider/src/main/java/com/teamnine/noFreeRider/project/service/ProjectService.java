package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.AddProjectDto;
import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project save(AddProjectDto addProjectDto) {
        return projectRepository.save(addProjectDto.toEntity());
    }
}
