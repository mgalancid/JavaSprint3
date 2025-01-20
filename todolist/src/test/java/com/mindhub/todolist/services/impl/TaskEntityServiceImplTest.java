package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.repositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskEntityServiceImplTest {

    @Mock
    private TaskEntityRepository taskRepository;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskEntityServiceImpl taskService;

    @Test
    void getTaskDTOById_whenIdFound_shouldReturnTask() {
        // Arrange
        TaskEntity task1 = new TaskEntity("Task 1",
                                            "Description of task 1",
                                            TaskEntity.TaskStatus.PENDING);

        ReflectionTestUtils.setField(task1, "id", 1L);

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        // Act
        TaskEntityDTO result = taskService.getTaskDTOById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Task 1", result.getTitle());
        assertEquals("Description of task 1", result.getDescription());

        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskDTOById_whenIdNotFound_shouldReturnError() {
        // Arrange
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskDTOById(1L);
        });

        // Assert
        verify(taskRepository).findById(1L);
    }

    @Test
    void getAllTasksDTO_whenAllTasksFound_shouldReturnAllTasks() {
        // Arrange
        TaskEntity task1 = new TaskEntity("Task 1",
                                            "Description of task 1",
                                            TaskEntity.TaskStatus.PENDING);

        TaskEntity task2 = new TaskEntity("Task 2",
                                            "Description of task 1",
                                            TaskEntity.TaskStatus.COMPLETED);
        List<TaskEntity> tasks = List.of(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<TaskEntityDTO> result = taskService.getAllTasksDTO();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void getAllTasksDTO_whenNoTasksFound_shouldReturnEmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TaskEntityDTO> result = taskService.getAllTasksDTO();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "The list of tasks should be empty.");

        verify(taskRepository).findAll();
    }

    @Test
    void updateTask_WhenTaskFound_ShouldUpdateTask() {
        // Arrange
        TaskEntity task1 = new TaskEntity("Task 1",
                "Description of task 1",
                TaskEntity.TaskStatus.PENDING);

        TaskEntity updatedTask = new TaskEntity("Updated Task",
                "Updated Description",
                TaskEntity.TaskStatus.COMPLETED);

        TaskEntityDTO updatedTaskDTO = new TaskEntityDTO(updatedTask);

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(updatedTask);

        // Act
        TaskEntityDTO result = taskService.updateTask(1L, updatedTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskEntity.TaskStatus.COMPLETED, result.getStatus());

        verify(taskRepository).findById(1L);
    }

    @Test
    void updateTask_WhenTaskNotFound_ShouldThrowTaskNotFoundException() {
        // Arrange
        TaskEntityDTO updatedTaskDTO = new TaskEntityDTO(new TaskEntity("Updated Task",
                                                                        "Updated Description",
                                                                        TaskEntity.TaskStatus.COMPLETED));

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        assertThrows(TaskNotFoundException.class, () ->
                taskService.updateTask(1L, updatedTaskDTO
                ));

        // Assert
        verify(taskRepository).findById(1L);
    }

    @Test
    public void createNewTask_WhenTaskIsCreated_ShouldReturnSavedTask() {
        // Arrange
        NewTaskEntityDTO newTaskDTO = new NewTaskEntityDTO("New Task",
                                                            "New Description",
                                                            TaskEntity.TaskStatus.PENDING);

        TaskEntity savedTask = new TaskEntity("New Task",
                                                    "New Description",
                                                    TaskEntity.TaskStatus.PENDING);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenReturn(savedTask);

        // Act
        NewTaskEntityDTO result = taskService.createNewTask(newTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskEntity.TaskStatus.PENDING, result.getStatus());

        Mockito.verify(taskRepository).save(Mockito.any(TaskEntity.class));
    }

    @Test
    public void createNewTask_WhenSavingFails_ShouldThrowTaskNotFoundException() {

        // Arrange
        NewTaskEntityDTO newTaskDTO = new NewTaskEntityDTO("New Task",
                "New Description",
                TaskEntity.TaskStatus.PENDING);

        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenThrow(
                new TaskNotFoundException("Error saving task"
                ));

        // Act
        assertThrows(TaskNotFoundException.class, () -> taskService.createNewTask(newTaskDTO));
    }

    @Test
    public void testAssignTask_WhenTaskAssignedToUser_ShouldReturnSucess() throws Exception {
        // Arrange
        UserEntity user = new UserEntity("John Doe", "12345678", "johndoe@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);

        TaskEntity task = new TaskEntity("Task 1", "Description", TaskEntity.TaskStatus.PENDING);
        ReflectionTestUtils.setField(task, "id", 1L);

        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));
        Mockito.when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(TaskEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskEntityDTO assignedTask = taskService.assignTask(authentication, 1L);

        // Assert
        assertNotNull(assignedTask, "Assigned task should not be null");
        assertEquals(user, task.getUserEntity(), "The task's user should be updated to the authenticated user");
    }

    @Test
    public void testAssignTask_WhenUserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Mockito.when(authentication.getName()).thenReturn("unknown_user");
        Mockito.when(userRepository.findByUsername("unknown_user")).thenReturn(java.util.Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            taskService.assignTask(authentication, 1L);
        });

        assertEquals("User with username unknown_user couldn't be found", exception.getMessage());
    }

    @Test
    public void testAssignTaskById_WhenUserExists_ShouldReturnTaskEntityDTO() throws UserNotFoundException {
        // Arrange
        UserEntity user = new UserEntity("John Doe",
                                        "12345678",
                                        "johndoe@example.com");

        ReflectionTestUtils.setField(user, "id", 1L);

        TaskEntity task = new TaskEntity("Test Task",
                                        "Task Description",
                                        TaskEntity.TaskStatus.PENDING);

        ReflectionTestUtils.setField(task, "id", 1L);
        task.setUserEntity(user);

        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));
        Mockito.when(taskRepository.findById(1L)).thenReturn(java.util.Optional.of(task));

        // Act
        TaskEntityDTO result = taskService.assignTaskById(authentication, 1L);

        // Assert
        assertNotNull(result, "TaskEntityDTO should not be null");
        assertEquals(task.getId(), result.getId(), "Task IDs should match");
        assertEquals(task.getTitle(), result.getTitle(), "Task titles should match");
        assertEquals(task.getDescription(), result.getDescription(), "Task descriptions should match");
        assertEquals(task.getStatus(), result.getStatus(), "Task statuses should match");
    }

    @Test
    public void testAssignTaskById_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() throws UserNotFoundException, TaskNotFoundException {
        // Arrange
        String username = "invalid_username";
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> taskService.assignTaskById(authentication, 1L));
    }

    @Test
    void deleteTask_WhenIDFound_ShouldDeleteTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenIDNotFound_ShouldNotDeleteTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(0)).deleteById(taskId);
    }
}