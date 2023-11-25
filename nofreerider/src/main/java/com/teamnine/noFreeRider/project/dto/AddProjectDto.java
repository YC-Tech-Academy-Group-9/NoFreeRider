package com.teamnine.noFreeRider.project.dto;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.domain.Project;

import java.util.Date;

public record AddProjectDto(
        String name,
        String summary,
        String className,
        Date startDate,
        Date endDate,
        Member leader
) {


    public Project toEntity() {
        return Project.builder()
                .project_name(name)
                .project_summary(summary)
                .className(className)
                .leader(leader)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
