package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.TaskEntityService;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import com.mindhub.todolist.services.impl.UserEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserEntityServiceImpl userService;

    @Autowired
    private TaskEntityServiceImpl taskService;

    public AdminController(UserEntityServiceImpl userService, TaskEntityServiceImpl taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/users") // Get All Users
    public List<UserEntityDTO> getAllUsers() {
        List<UserEntityDTO> userDTOS = userService.getAllUsersDTO();
        return ResponseEntity.ok(userDTOS).getBody();
    }

    @GetMapping("users/{id}") // Get User By ID
    public ResponseEntity<UserEntityDTO> getUserDTOById(@PathVariable Long id) throws UserNotFoundException {
        UserEntityDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/tasks")
    public List<TaskEntityDTO> getAllTasks () {
        List<TaskEntityDTO> taskDTO = taskService.getAllTasksDTO();
        return ResponseEntity.ok(taskDTO).getBody();
    }

    @GetMapping("tasks/{id}")
    public ResponseEntity<TaskEntityDTO> getTaskDTOByID(@PathVariable Long id) throws TaskNotFoundException {
        TaskEntityDTO taskDTO = taskService.getTaskDTOById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id){
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
    }
}
