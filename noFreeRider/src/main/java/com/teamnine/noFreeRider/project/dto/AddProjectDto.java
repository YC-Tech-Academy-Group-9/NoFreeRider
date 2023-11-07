package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.domain.Project;

public record AddProjectDto(
        String name,
        String summary,
        String className,
        Member leader
) {

    public Project toEntity() {
        return Project.builder()
                .project_name(name)
                .project_summary(summary)
                .class_name(className)
                .leader(leader)
                .build();
    }
}
