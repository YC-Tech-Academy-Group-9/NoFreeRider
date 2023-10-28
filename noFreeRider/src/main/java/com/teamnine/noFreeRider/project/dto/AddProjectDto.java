package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.Member.domain.Member;
import com.teamnine.noFreeRider.project.domain.Project;

public record AddProjectDto(
        String name,
        String summary,
        Member leader
) {

    public Project toEntity() {
        return Project.builder()
                .project_name(name)
                .project_summary(summary)
                .leader(leader)
                .build();
    }
}
