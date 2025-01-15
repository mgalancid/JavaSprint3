package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTaskEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import com.mindhub.todolist.services.impl.UserEntityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {
    @Autowired
    private UserEntityServiceImpl userService;

    @Autowired
    private TaskEntityServiceImpl taskService;

    public UserEntityController(UserEntityServiceImpl userService,
                                TaskEntityServiceImpl taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @Operation(summary = "Upload User By ID", description = "Uploads the information of an user by an given ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The data to be uploaded.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully uploaded."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data.")
    })
    @PutMapping("/tasks/{id}") // Upload Task By ID
    public ResponseEntity<TaskEntityDTO> updateTaskById(@PathVariable(name = "id") Long id,
                                                        @RequestBody TaskEntityDTO taskDTO) throws UserNotFoundException {
        TaskEntityDTO task = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Create New Task", description = "Creates a new Task object from.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The data to be created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data."),
            @ApiResponse(responseCode = "409", description = "Conflict data.")
    })
    @PostMapping("tasks") // Create Task
    public ResponseEntity<NewTaskEntityDTO> createNewTask(@RequestBody NewTaskEntityDTO newTaskDTO) {

        NewTaskEntityDTO createdTaskDTO = taskService.createNewTask(newTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }

    @Operation(summary = "Assign Task", description = "Assigns a tasks to a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User got assigned with a task.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't find user.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class)))
    })
    @PutMapping("/{id}/assign") // Assign Task
    public ResponseEntity<TaskEntityDTO> assignTask(@PathVariable Long id, @RequestBody UserEntityDTO userDTO) {
        TaskEntityDTO assignedTask = taskService.assignTask(id, userDTO);
        return ResponseEntity.ok(assignedTask);
    }

    @Operation(summary = "Delete User By Email", description = "Deletes an user by Email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User got deleted from the database."),
            @ApiResponse(responseCode = "400", description = "User wasn't deleted.")
    })
    @DeleteMapping // Delete User by Email
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
