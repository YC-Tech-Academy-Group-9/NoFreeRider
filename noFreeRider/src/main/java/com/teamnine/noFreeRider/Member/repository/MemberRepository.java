package com.teamnine.noFreeRider.Member.repository;

import com.teamnine.noFreeRider.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMemberEmail(String memberEmail);
}
