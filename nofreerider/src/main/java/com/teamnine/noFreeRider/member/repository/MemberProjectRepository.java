package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.MemberProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject,Long> {

    public boolean existsByMemberIdAndProjectId(UUID member_id, UUID project_id);

    public Optional<MemberProject> findByMemberIdAndProjectId(UUID member_id, UUID project_id);

    public Optional<List<MemberProject>> findAllByMemberId(UUID member_id);

    public Optional<List<MemberProject>> findAllByProjectId(UUID project_id);

    public void deleteAllByProjectId(UUID project_id);
}
