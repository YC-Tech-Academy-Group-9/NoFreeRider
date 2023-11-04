package com.teamnine.noFreeRider.Member.repository;

import com.teamnine.noFreeRider.Member.domain.MemberProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject,Long> {
}
