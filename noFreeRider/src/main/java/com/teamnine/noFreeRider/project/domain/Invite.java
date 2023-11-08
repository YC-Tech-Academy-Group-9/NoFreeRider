package com.teamnine.noFreeRider.project.domain;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.UUID;

@RedisHash(value = "Invite", timeToLive = 604800) // 일주일
public class Invite {
    @Id
    private String id;
    private UUID projectId;
    private UUID inviteCode;
    private boolean isInvited;

    public Invite(UUID projectId) {
        this.projectId = projectId;
        this.inviteCode = UUID.randomUUID();
        this.isInvited = false;
    }
}
