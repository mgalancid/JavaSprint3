package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.UserNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserEntityController {
    @Autowired
    private UserEntityServiceImpl userService;

    public UserEntityController(UserEntityServiceImpl userService) {
        this.userService = userService;
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
    @GetMapping("/{id}") // Get User By ID
    public ResponseEntity<UserEntityDTO> getUserDTOById(@PathVariable Long id) throws UserNotFoundException {
        UserEntityDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Get All Users", description = "Retrieves all users from the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got response.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserEntityDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No users found.",
                    content = @Content)
    })
    @GetMapping // Get All Users
    public ResponseEntity<List<UserEntityDTO>> getAllUsersDTO() {
        List<UserEntityDTO> userDTOS = userService.getAllUsersDTO();
        return ResponseEntity.ok(userDTOS);
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
    @PutMapping("/{id}") // Upload User By ID
    public ResponseEntity<UserEntityDTO> uploadUserDTOById(@PathVariable Long id,
                                        @RequestBody UserEntityDTO userDetailsDTO) throws UserNotFoundException {
        UserEntityDTO updatedUser = userService.updateUser(id, userDetailsDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Create New User", description = "Creates a new User object from.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The data to be created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntityDTO.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data."),
            @ApiResponse(responseCode = "409", description = "Conflict data.")
    })
    @PostMapping // Create New User
    public ResponseEntity<UserEntityDTO> createNewUser(@RequestBody UserEntityDTO userDTO) {
        UserEntityDTO createdUser = userService.createNewUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
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
    @DeleteMapping("/{id}") // Create New User
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
