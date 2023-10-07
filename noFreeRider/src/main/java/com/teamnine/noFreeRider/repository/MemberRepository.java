package com.teamnine.noFreeRider.repository;

import com.teamnine.noFreeRider.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
