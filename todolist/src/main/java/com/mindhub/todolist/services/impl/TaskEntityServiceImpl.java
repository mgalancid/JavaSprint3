package com.mindhub.todolist.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.services.TaskEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskEntityServiceImpl implements TaskEntityService {

    @Autowired
    private TaskEntityRepository taskRepository;
    private ObjectMapper objectMapper;

    public TaskEntityServiceImpl(TaskEntityRepository taskRepository,
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
    public TaskEntityDTO createNewTask(TaskEntityDTO taskEntityDTO) {
        TaskEntity taskEntity = objectMapper.convertValue(taskEntityDTO, TaskEntity.class);
        TaskEntity savedTask = taskRepository.save(taskEntity);
        return objectMapper.convertValue(savedTask, TaskEntityDTO.class);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
