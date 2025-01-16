package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskEntityService {
    TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException;
    List<TaskEntityDTO> getAllTasksDTO();
    TaskEntityDTO updateTask(Long id, TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException;
    NewTaskEntityDTO createNewTask(NewTaskEntityDTO newTaskDTO);
    TaskEntityDTO assignTask(Authentication authentication, Long taskId) throws UserNotFoundException, TaskNotFoundException;
    TaskEntityDTO assignTaskById(Authentication authentication, Long taskId) throws UserNotFoundException, TaskNotFoundException;
    void deleteTask(Long id);
}
