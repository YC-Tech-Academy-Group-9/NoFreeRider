package com.teamnine.noFreeRider.member.repository;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberTask;
import com.teamnine.noFreeRider.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTaskRepository extends JpaRepository<MemberTask, Long> {
    List<MemberTask> findAllByTask(Task task);
    List<MemberTask> findAllByMember(Member member);
}
