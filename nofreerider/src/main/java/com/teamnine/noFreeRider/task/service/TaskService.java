package com.teamnine.noFreeRider.task.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberTask;
import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.repository.MemberTaskRepository;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.repository.ProjectRepository;
import com.teamnine.noFreeRider.project.service.ProjectService;
import com.teamnine.noFreeRider.task.domain.Task;
import com.teamnine.noFreeRider.task.dto.TaskCreateDto;
import com.teamnine.noFreeRider.task.dto.TaskDisplayDto;
import com.teamnine.noFreeRider.task.dto.TaskDto;
import com.teamnine.noFreeRider.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final MemberTaskRepository memberTaskRepository;
    private final ProjectRepository projectRepository;

    public List<TaskDisplayDto> searchTasksByProjectId(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 입니다."));
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.sort(Comparator.comparing(Task::getStatus_code)
                .thenComparing(Task::getDue_date));
        return tasks.stream().map(task -> new TaskDisplayDto(
                task.getId(),
                task.getTaskName(),
                task.getTaskContent(),
                task.getDue_date(),
                task.getStatus_code(),
                getMembersByTask(task),
                task.getProject().getId()
        )).toList();
    }

    public List<TaskDisplayDto> searchTasksByMemberId(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버 입니다."));
        List<MemberTask> memberTasks = memberTaskRepository.findAllByMember(member);
        List<Task> tasks = memberTasks.stream()
                .map(MemberTask::getTask)
                .sorted(Comparator.comparing(Task::getStatus_code)
                        .thenComparing(Task::getDue_date))
                .toList();
        return tasks.stream().map(task -> new TaskDisplayDto(
                task.getId(),
                task.getTaskName(),
                task.getTaskContent(),
                task.getDue_date(),
                task.getStatus_code(),
                getMembersByTask(task),
                task.getProject().getId()
        )).toList();
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
        Project targetProject = projectRepository.findById(taskDto.projectId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트 입니다."));
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
        Task taskToDelete = taskRepository.getById(taskId);
        memberTaskRepository.deleteAllByTask(taskToDelete);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void deleteAllByProject(Project project) {
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(memberTaskRepository::deleteAllByTask);
        taskRepository.deleteAllByProject(project);
    }

    public Project getProjectByTaskId(UUID taskId) {
        Task task = getTaskById(taskId);
        return task.getProject();
    }
}
