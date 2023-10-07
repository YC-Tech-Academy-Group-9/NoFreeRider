package com.teamnine.noFreeRider.Project.repository;

import com.teamnine.noFreeRider.Project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
