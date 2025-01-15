package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;

import java.util.List;

public interface TaskEntityService {
    TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException;
    List<TaskEntityDTO> getAllTasksDTO();
    TaskEntityDTO updateTask(Long id, TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException;
    NewTaskEntityDTO createNewTask(NewTaskEntityDTO newTaskDTO);
    TaskEntityDTO assignTask(Long id, UserEntityDTO userDTO) throws UserNotFoundException;
    TaskEntityDTO assignTaskById(Long id, Long userId) throws UserNotFoundException;
    void deleteTask(Long id);
}
