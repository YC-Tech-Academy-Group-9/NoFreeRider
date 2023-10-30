package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMemberEmail(String memberEmail);
}
