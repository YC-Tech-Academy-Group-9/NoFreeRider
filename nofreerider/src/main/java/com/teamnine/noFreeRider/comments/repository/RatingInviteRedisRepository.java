package com.teamnine.noFreeRider.comments.repository;

import com.teamnine.noFreeRider.comments.domain.RatingInvite;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RatingInviteRedisRepository extends CrudRepository<RatingInvite, String> {
    Optional<RatingInvite> findRatingInviteByInviteCode(UUID inviteCode);

//    Optional<Invite> findInviteByProjectId(UUID projectId);

//    Optional<Invite> findInviteByInviteCode(UUID inviteCode);

//    Optional<Invite> findAllBy(UUID projectId, boolean isInvited);

}
