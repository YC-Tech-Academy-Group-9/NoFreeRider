package com.teamnine.noFreeRider.project.domain;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.util.UUID;

@Getter
@RedisHash(value = "Invite", timeToLive = 604800) // 일주일
public class Invite {
    @Id
    private String id;
    @Indexed
    private UUID projectId;
    @Indexed
    private UUID inviteCode;
    @Indexed
    private boolean isInvited;

    public Invite(UUID projectId) {
        this.projectId = projectId;
        this.inviteCode = UUID.randomUUID();
        this.isInvited = false;
    }

    public void use() {
        this.isInvited = true;
    }
}
