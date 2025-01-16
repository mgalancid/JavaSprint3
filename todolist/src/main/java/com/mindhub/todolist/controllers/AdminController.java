package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UnauthorizedUserException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
                            schema = @Schema(implementation = UserEntityDTO.class))),
            @ApiResponse(responseCode = "403", description = "ForbiddenAccess.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class)))
    })
    @GetMapping("/users") // Get All Users
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserEntityDTO>> getAllUsers() {
        try {
            List<UserEntityDTO> userDTOS = userService.getAllUsersDTO();
            return ResponseEntity.ok(userDTOS);
        } catch (AccessDeniedException e) {
            throw new UnauthorizedUserException("You don't have permission to access this route");        }
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
    @GetMapping("/users/{id}") // Get User By ID
    public ResponseEntity<?> getUserById(Authentication authentication, @PathVariable Long id) throws UserNotFoundException {
        authentication.getName();
        try {
            UserEntityDTO user = userService.getUserDTOById(id);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found.");
        }
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

    @Operation(summary = "Assign Task by ID", description = "Assigns a tasks to a user by using it's ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User got assigned with a task.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't find user.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class)))
    })
    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<TaskEntityDTO> assignTaskById(@PathVariable Long id, Authentication authentication) {
        TaskEntityDTO assignedTask = null;
        try {
            assignedTask = taskService.assignTaskById(authentication, id);
        } catch (UserNotFoundException | TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(assignedTask);
    }

    @Operation(summary = "Delete User By Id", description = "Deletes an user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User got deleted from the database."),
            @ApiResponse(responseCode = "400", description = "User wasn't deleted.")
    })
    @DeleteMapping("/users/{id}") // Delete Admin By ID
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
    }
}
