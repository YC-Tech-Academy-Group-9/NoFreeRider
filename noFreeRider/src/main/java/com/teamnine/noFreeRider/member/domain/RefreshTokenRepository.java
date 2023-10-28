package com.teamnine.noFreeRider.Member.domain;

import com.teamnine.noFreeRider.Member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByMember_memberEmail(String email);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
