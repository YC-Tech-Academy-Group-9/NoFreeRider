package com.teamnine.noFreeRider.task.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberTask;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.repository.MemberTaskRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.service.ProjectService;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.dto.TaskCreateDto;
import com.teamnine.noFreeRider.task.dto.TaskDisplayDto;
import com.teamnine.noFreeRider.task.dto.TaskDto;
import com.teamnine.noFreeRider.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final MemberTaskRepository memberTaskRepository;
    private final ProjectService projectService;

    public List<TaskDisplayDto> searchTasksByProjectId(UUID projectId) {
        Project project = projectService.getProjectInfo(projectId);
        List<Task> tasks = taskRepository.findAllByProject(project);
        return tasks.stream().map(task -> new TaskDisplayDto(
                task.getId(),
                task.getTaskName(),
                task.getTaskContent(),
                task.getDue_date(),
                task.getStatus_code(),
                getMembersByTask(task)
        )).toList();
        // map tasks to TaskDisplayDto

    }

    public List<String> getMembersByTask(Task task) {
        List<MemberTask> memberTasks = memberTaskRepository.findAllByTask(task);
        return memberTasks.stream().map(MemberTask -> MemberTask.getMember().getMemberName()).toList();
    }

    public Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태스크 입니다."));
    }

    public void giveTaskToMember(UUID taskId, int studentId) {
        Member member = memberRepository.findByMemberStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버 입니다."));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태스크 입니다."));
        MemberTask memberTask = new MemberTask(task, member);
        memberTaskRepository.save(memberTask);
    }

    public Task createTask(TaskCreateDto taskDto) {
        Project targetProject = projectService.getProjectInfo(taskDto.projectId());
        Task task = new Task(
                targetProject,
                taskDto.taskName(),
                taskDto.taskContent(),
                taskDto.dueDateTime()
        );
        Task newTask = taskRepository.save(task);
        taskDto.memberIds().forEach(memberStudentId -> giveTaskToMember(newTask.getId(), memberStudentId));
        return newTask;
    }

    @Transactional
    public Task updateTask(UUID taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태스크 입니다."));
        task.updateTask(taskDto);
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }
}
