package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.TaskEntityService;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import com.mindhub.todolist.services.impl.UserEntityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get All Users", description = "Retrieves all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all users.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't find all users.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class)))
    })
    @GetMapping("/users") // Get All Users
    public List<UserEntityDTO> getAllUsers() {
        List<UserEntityDTO> userDTOS = userService.getAllUsersDTO();
        return ResponseEntity.ok(userDTOS).getBody();
    }

    @Operation(summary = "Get User ID", description = "Retrieves the id of an User.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User ID was found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "User's ID could not be found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class)))
    })
    @GetMapping("users/{id}") // Get User By ID
    public ResponseEntity<UserEntityDTO> getUserDTOById(@PathVariable Long id) throws UserNotFoundException {
        UserEntityDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Get All Tasks", description = "Retrieves all tasks from users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all tasks.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't find all tasks.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class)))
    })
    @GetMapping("/tasks")
    public List<TaskEntityDTO> getAllTasks () {
        List<TaskEntityDTO> taskDTO = taskService.getAllTasksDTO();
        return ResponseEntity.ok(taskDTO).getBody();
    }

    @Operation(summary = "Get Task ID", description = "Retrieves the id of a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task ID was found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Task's ID could not be found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class)))
    })
    @GetMapping("tasks/{id}") // Get Task by ID
    public ResponseEntity<TaskEntityDTO> getTaskDTOByID(@PathVariable Long id) throws TaskNotFoundException {
        TaskEntityDTO taskDTO = taskService.getTaskDTOById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id){
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
    }
}
