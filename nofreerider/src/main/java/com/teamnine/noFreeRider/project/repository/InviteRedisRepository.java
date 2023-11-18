package com.teamnine.noFreeRider.project.repository;

import com.teamnine.noFreeRider.project.domain.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRedisRepository extends CrudRepository<Invite, String> {
    public Optional<Invite> findByProjectIdAndInviteCode(UUID projectId, UUID inviteCode);

}
