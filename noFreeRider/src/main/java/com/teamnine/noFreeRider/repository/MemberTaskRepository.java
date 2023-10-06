package com.teamnine.noFreeRider.repository;

import com.teamnine.noFreeRider.domain.MemberTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTaskRepository extends JpaRepository<MemberTask, Long> {
}
