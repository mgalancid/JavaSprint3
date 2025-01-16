package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskEntityRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByStatus(TaskEntity.TaskStatus status);
    List<TaskEntity> findByUserEntityAndStatus(UserEntity user, TaskEntity.TaskStatus status);
    List<TaskEntity> findByUserEntity(UserEntity user);
    List<TaskEntity> findByTitle(String title);
    Long countByStatus(TaskEntity.TaskStatus status);
}
