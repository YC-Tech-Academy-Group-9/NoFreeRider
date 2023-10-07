package com.teamnine.noFreeRider.Task.repository;

import com.teamnine.noFreeRider.Task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
