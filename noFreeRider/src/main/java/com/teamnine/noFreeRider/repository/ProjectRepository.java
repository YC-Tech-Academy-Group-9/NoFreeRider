package com.teamnine.noFreeRider.repository;

import com.teamnine.noFreeRider.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
