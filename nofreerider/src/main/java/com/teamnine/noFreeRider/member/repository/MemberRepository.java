package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByMemberEmail(String member_email);
    Optional<Member> findByMemberStudentId(int member_student_id);
}
