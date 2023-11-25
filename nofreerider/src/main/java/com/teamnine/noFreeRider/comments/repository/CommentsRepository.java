package com.teamnine.noFreeRider.comments.repository;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<UserComment, UUID> {

    Optional<UserComment> findByMember(Member member);

}
