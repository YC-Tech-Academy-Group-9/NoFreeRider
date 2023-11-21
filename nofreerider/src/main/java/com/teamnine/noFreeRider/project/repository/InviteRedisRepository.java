package com.teamnine.noFreeRider.project.repository;

import com.teamnine.noFreeRider.project.domain.Invite;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRedisRepository extends CrudRepository<Invite, String> {
//    Optional<Invite> findInviteByProjectIdAndInviteCode(UUID projectId, UUID inviteCode);

//    Optional<Invite> findInviteByProjectId(UUID projectId);

//    Optional<Invite> findInviteByInviteCode(UUID inviteCode);

//    Optional<Invite> findAllBy(UUID projectId, boolean isInvited);

}
