package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public interface TaskEntityService {
    TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException;
    List<TaskEntityDTO> getAllTasksDTO();
    TaskEntityDTO updateTask(Long id, TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException;
    TaskEntityDTO createNewTask(NewTaskEntityDTO newTaskDTO);
    TaskEntityDTO assignTask(Long id, UserEntityDTO userDTO);
    TaskEntityDTO assignTaskById(Long id, Long userId);
    void deleteTask(Long id);
}
