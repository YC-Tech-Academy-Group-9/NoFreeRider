package com.teamnine.noFreeRider.comments.domain;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.util.UUID;

@Getter
@RedisHash(value = "RatingInvite", timeToLive = 604800) // 일주일
public class RatingInvite {
    @Id
    private String id;
    @Indexed
    private UUID projectId;
    @Indexed
    private UUID inviteCode;
    @Indexed
    private boolean hasRated;

    public RatingInvite(UUID projectId) {
        this.projectId = projectId;
        this.inviteCode = UUID.randomUUID();
        this.hasRated = false;
    }

    public void use() {
        this.hasRated = true;
    }
}
