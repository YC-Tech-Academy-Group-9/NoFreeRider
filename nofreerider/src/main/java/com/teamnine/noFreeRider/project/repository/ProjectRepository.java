package com.teamnine.noFreeRider.project.repository;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}
