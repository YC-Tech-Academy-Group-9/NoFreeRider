package com.teamnine.noFreeRider.task.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberTask;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.repository.MemberTaskRepository;
import com.teamnine.noFreeRider.project.dto.SearchTaskListDto;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final MemberTaskRepository memberTaskRepository;

    public SearchTaskListDto searchTasksByProjectId(UUID projectId) {
        return new SearchTaskListDto(taskRepository.findAllByProjectId(projectId));
    }

    public void giveTaskToMember(UUID taskId, UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
        MemberTask memberTask = new MemberTask(task, member);
        memberTaskRepository.save(memberTask);
    }
}
