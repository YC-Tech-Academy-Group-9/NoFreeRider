package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.MemberProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject,Long> {

    public boolean existsByMemberIdAndProjectId(UUID member_id, UUID project_id);

    public Long findIdByMemberIdAndProjectId(UUID member_id, UUID project_id);
}
