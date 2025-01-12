package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import java.util.List;

public interface TaskEntityService {
    TaskEntityDTO getTaskDTOById(Long id) throws TaskNotFoundException;
    List<TaskEntityDTO> getAllTasksDTO();
    TaskEntityDTO updateTask(Long id, TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException;
    TaskEntityDTO createNewTask(TaskEntityDTO taskEntityDTO);
    void deleteTask(Long id);

}
