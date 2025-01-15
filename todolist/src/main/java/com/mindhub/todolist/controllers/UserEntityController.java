package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUserEntityDTO;
import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import com.mindhub.todolist.services.impl.UserEntityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Get User's username", description = "Retrieves the email of an user")
    @GetMapping("/username")
    public String getUserName(Authentication authentication){
        return authentication.getName();
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
}
