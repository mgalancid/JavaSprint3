package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskEntityRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private TaskEntityRepository taskRepository;

    @Test
    void testFindByStatus_WhenGivenStatus_ShouldReturnTrue() {
        // Arrange
        TaskEntity task = new TaskEntity("Task1", "Description1", TaskEntity.TaskStatus.PENDING);
        entityManager.persistAndFlush(task);

        // Act
        List<TaskEntity> result = taskRepository.findByStatus(TaskEntity.TaskStatus.PENDING);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(TaskEntity.TaskStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void testFindByStatus_WhenStatusNotFound_ShouldReturnFalse() {
        // Act
        List<TaskEntity> result = taskRepository.findByStatus(TaskEntity.TaskStatus.COMPLETED);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByUserEntityAndStatus_WhenGivenUser_ShouldReturnTrue() {
        // Arrange
        UserEntity user = new UserEntity();
        entityManager.persistAndFlush(user);

        TaskEntity task = new TaskEntity("Task1", "Description1", TaskEntity.TaskStatus.IN_PROCESS);
        task.setUserEntity(user);
        entityManager.persistAndFlush(task);

        // Act
        List<TaskEntity> result = taskRepository.findByUserEntityAndStatus(user, TaskEntity.TaskStatus.IN_PROCESS);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUserEntity().getId());
    }

    @Test
    void testFindByUserEntityAndStatus_WhenUserNotFound_ShouldReturnFalse() {
        // Arrange
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");
        existingUser.setEmail("existing@example.com");
        entityManager.persistAndFlush(existingUser);

        TaskEntity task = new TaskEntity("Task1", "Description1", TaskEntity.TaskStatus.PENDING);
        task.setUserEntity(existingUser);
        entityManager.persistAndFlush(task);

        UserEntity nonExistentUser = new UserEntity();

        // Act
        List<TaskEntity> result = taskRepository.findByUserEntityAndStatus(existingUser, TaskEntity.TaskStatus.PENDING);

        // Assert
        assertFalse(result.contains(existingUser));
    }

    @Test
    void testFindByTitle_WhenGivenTitle_ShouldReturnTrue() {
        // Arrange
        TaskEntity task = new TaskEntity("UniqueTitle", "Description", TaskEntity.TaskStatus.PENDING);
        entityManager.persistAndFlush(task);

        // Act
        List<TaskEntity> result = taskRepository.findByTitle("UniqueTitle");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("UniqueTitle", result.get(0).getTitle());
    }

    @Test
    void testFindByTitle_WhenTitleNotFound_ShouldReturnFalse() {
        // Act
        List<TaskEntity> result = taskRepository.findByTitle("NonExistentTitle");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByStatus_WhenGivenStatus_ShouldReturnCorrectCount() {
        // Arrange
        TaskEntity task1 = new TaskEntity("Task 1", "Description 1", TaskEntity.TaskStatus.PENDING);
        TaskEntity task2 = new TaskEntity("Task 2", "Description 2", TaskEntity.TaskStatus.PENDING);
        TaskEntity task3 = new TaskEntity("Task 3", "Description 3", TaskEntity.TaskStatus.COMPLETED);

        entityManager.persistAndFlush(task1);
        entityManager.persistAndFlush(task2);
        entityManager.persistAndFlush(task3);

        // Act
        Long countPending = taskRepository.countByStatus(TaskEntity.TaskStatus.PENDING);
        Long countCompleted = taskRepository.countByStatus(TaskEntity.TaskStatus.COMPLETED);

        // Assert
        assertEquals(2, countPending);
        assertEquals(1, countCompleted);
    }

    @Test
    void testCountByStatus_WhenNoTasksWithStatus_ShouldReturnZero() {
        // Arrange
        TaskEntity task1 = new TaskEntity("Task 1", "Description 1", TaskEntity.TaskStatus.IN_PROCESS);
        TaskEntity task2 = new TaskEntity("Task 2", "Description 2", TaskEntity.TaskStatus.IN_PROCESS);

        entityManager.persistAndFlush(task1);
        entityManager.persistAndFlush(task2);

        // Act
        Long countPending = taskRepository.countByStatus(TaskEntity.TaskStatus.PENDING);

        // Assert
        assertEquals(0, countPending);
    }
}