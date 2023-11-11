package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.MemberTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTaskRepository extends JpaRepository<MemberTask, Long> {
}
