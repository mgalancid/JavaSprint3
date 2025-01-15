package com.mindhub.todolist.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
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
    private ObjectMapper objectMapper;

    public TaskEntityServiceImpl(TaskEntityRepository taskRepository,
                                 UserEntityRepository userRepository,
                                 ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
    }
    @Override
    public TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException {
        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task not found with ID: " + id)
        );
        return objectMapper.convertValue(task, TaskEntityDTO.class);
    }

    @Override
    public List<TaskEntityDTO> getAllTasksDTO() {
        List<TaskEntity> task = taskRepository.findAll();
        return task.stream()
                    .map(
                            taskEntity -> objectMapper.convertValue(taskEntity,
                                                                                TaskEntityDTO.class)
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
        return objectMapper.convertValue(existingTask, TaskEntityDTO.class);
    }

    @Override
    public TaskEntityDTO createNewTask(NewTaskEntityDTO newTaskDTO) {

        TaskEntity taskEntity = new TaskEntity(newTaskDTO.getTitle(),
                                                newTaskDTO.getDescription(),
                                                newTaskDTO.getStatus());
        TaskEntity savedTask = taskRepository.save(taskEntity);
        return objectMapper.convertValue(savedTask, TaskEntityDTO.class);
    }

    @Override
    public TaskEntityDTO assignTask(Long id, UserEntity user) {
       TaskEntity task = taskRepository.findById(id).orElseThrow(
               () -> new TaskNotFoundException("Task couldn't be found")
       );
       task.setUserEntity(user);
       TaskEntity savedTask = taskRepository.save(task);
       return new TaskEntityDTO(savedTask);
    }

    @Override
    public TaskEntityDTO assignTaskById(Long id, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new TaskNotFoundException("Task couldn't be found")
        );
        return assignTaskById(id, userId);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
