package com.teamnine.noFreeRider.project.repository;

import com.teamnine.noFreeRider.project.domain.Invite;
import org.springframework.data.repository.CrudRepository;

public interface InviteRedisRepository extends CrudRepository<Invite, String> {
}
