package com.teamnine.noFreeRider.Member.domain;

import com.teamnine.noFreeRider.Member.repository.MemberTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTaskRepository extends JpaRepository<MemberTask, Long> {
}
