package com.teamnine.noFreeRider.task.repository;

import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    public List<Task> findAllByProject(Project project);

}
