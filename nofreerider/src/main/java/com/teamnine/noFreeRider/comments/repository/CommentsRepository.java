package com.teamnine.noFreeRider.comments.repository;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<UserComment, UUID> {
    Optional<UserComment> findByUserId(int userId);
}
