package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.repositories.UserEntityRepository;
import com.mindhub.todolist.services.TaskEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskEntityServiceImpl implements TaskEntityService {

    @Autowired
    private TaskEntityRepository taskRepository;

    @Autowired
    private UserEntityRepository userRepository;

    public TaskEntityServiceImpl(TaskEntityRepository taskRepository,
                                 UserEntityRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Override
    public TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException {
        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task not found with ID: " + id)
        );
        return new TaskEntityDTO(task);
    }

    @Override
    public List<TaskEntityDTO> getAllTasksDTO() {
        List<TaskEntity> task = taskRepository.findAll();
        return task.stream()
                    .map(
                            taskEntity ->  new TaskEntityDTO(taskEntity)
                    )
                    .collect(Collectors.toList());
    }

    @Override
    public TaskEntityDTO updateTask(Long id, TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException {
        TaskEntity existingTask = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task not found with ID: " + id)
        );
        existingTask.setTitle(taskDetailsDTO.getTitle());
        existingTask.setDescription(taskDetailsDTO.getDescription());
        existingTask.setStatus(taskDetailsDTO.getStatus());
        TaskEntity updatedTask = taskRepository.save(existingTask);
        return new TaskEntityDTO(existingTask);
    }

    @Override
    public NewTaskEntityDTO createNewTask(NewTaskEntityDTO newTaskDTO) {
        TaskEntity taskEntity = new TaskEntity(newTaskDTO.getTitle(),
                newTaskDTO.getDescription(),
                newTaskDTO.getStatus());
        TaskEntity savedTask = taskRepository.save(taskEntity);
        return new NewTaskEntityDTO(savedTask.getTitle(),
                                    savedTask.getDescription(),
                                    savedTask.getStatus());
    }

    @Override
    public TaskEntityDTO assignTask(Long id, UserEntityDTO userDTO) throws UserNotFoundException {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " couldn't be found"));

        UserEntity user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userDTO.getId() + " couldn't be found"));

        task.setUserEntity(user);
        TaskEntity savedTask = taskRepository.save(task);
        return new TaskEntityDTO(savedTask);
    }


    @Override
    public TaskEntityDTO assignTaskById(Long id, Long userId) throws UserNotFoundException {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new TaskNotFoundException("Task couldn't be found")
        );
        UserEntityDTO userDTO = new UserEntityDTO(user);
        return assignTask(id, userDTO);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
