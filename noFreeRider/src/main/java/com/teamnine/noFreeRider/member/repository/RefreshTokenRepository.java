package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByMember_memberEmail(String memberEmail);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
