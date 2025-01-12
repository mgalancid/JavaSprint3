package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskEntityController {
    @Autowired
    private TaskEntityServiceImpl taskService;

    public TaskEntityController(TaskEntityServiceImpl taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get Task ID", description = "Retrieves the id of a Task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task ID was found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))),
    })
    @GetMapping("/{id}")
    public TaskEntityDTO getTaskById(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.getTaskDTOById(id);
    }

    @Operation(summary = "Get All Tasks", description = "Retrieves all tasks from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got response.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskEntityDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No tasks found.",
                    content = @Content)
    })
    @GetMapping
    public List<TaskEntityDTO> getAllTasksDTO() {
        return taskService.getAllTasksDTO();
    }

    @Operation(summary = "Upload Task By ID", description = "Uploads the information of a task by an given ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The data to be uploaded.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskEntityDTO.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully uploaded."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data.")
    })
    @PutMapping("/{id}")
    public TaskEntityDTO updateTaskDTO(@PathVariable Long id,
                                       @RequestBody TaskEntityDTO taskDetailsDTO) throws TaskNotFoundException{
        return taskService.updateTask(id, taskDetailsDTO);
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
    @PostMapping
    public TaskEntityDTO createNewTask(@RequestBody TaskEntityDTO taskDTO) {
        return taskService.createNewTask(taskDTO);
    }

    @Operation(summary = "Delete User",
            description = "Deletes the User with the given ID.",
            parameters = {
                    @Parameter(name = "id", description = "The ID of the user to delete",
                            schema = @Schema(type = "long"))
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }


}
