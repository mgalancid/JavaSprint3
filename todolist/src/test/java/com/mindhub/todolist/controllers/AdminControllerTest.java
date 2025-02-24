package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.TaskEntityDTO;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.TaskNotFoundException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.RegisterUser;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserEntityRepository;
import com.mindhub.todolist.services.impl.TaskEntityServiceImpl;
import com.mindhub.todolist.services.impl.UserEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityRepository userRepository;

    @MockBean
    private UserEntityServiceImpl userService;

    @MockBean
    private TaskEntityServiceImpl taskService;

    @BeforeEach
    public void setUp() {
        RegisterUser adminUser = new RegisterUser("admin", "admin@example.com", "password");
        UserEntity savedUser = new UserEntity(adminUser.username(), adminUser.password(), adminUser.email());
        savedUser.setRole(RoleType.ADMIN);
        userRepository.save(savedUser);
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = "ADMIN")
    void testGetAllUsers_ReturnsUserDTOList_WhenRequestIsValid() throws Exception {
        // Arrange
        List<UserEntityDTO> userDTOs = createSampleUserDTOs();

        Mockito.when(userService.getAllUsersDTO()).thenReturn(userDTOs);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(userDTOs.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("user1@example.com"));

        verify(userService).getAllUsersDTO();
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = "ADMIN")
    void testGetAllUsers_WhenNoUsersFound_ShouldReturnEmptyList() throws Exception {
        // Arrange
        List<UserEntityDTO> emptyUserDTOs = List.of();

        // Act
        Mockito.when(userService.getAllUsersDTO()).thenReturn(emptyUserDTOs);

        // Assert
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ensure response status is 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

        verify(userService).getAllUsersDTO();
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = "ADMIN")
    public void testAssignTaskById_WhenUserFound_ShouldAssignTask() throws Exception {
        // Arrange
        TaskEntity mockTask = new TaskEntity();
        ReflectionTestUtils.setField(mockTask, "id", 1L);

        // Act
        Mockito.when(taskService.assignTaskById(Mockito.any(), Mockito.anyLong()))
                .thenReturn(
                        new TaskEntityDTO(mockTask
                        ));

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/1/assign/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin@example.com", authorities = "ADMIN")
    public void testAssignTaskById_WhenUserNotFound_ShouldReturnNull() throws Exception {

        // Act & Assert
        Mockito.when(taskService.assignTaskById(Mockito.any(), Mockito.anyLong()))
                .thenThrow(
                        new TaskNotFoundException("Task not found"
                ));

        mockMvc.perform(MockMvcRequestBuilders.put("/1/assign/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteUser_WhenUserGetsDelete_ShouldReturnSuccess() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /// Helper

    private List<UserEntityDTO> createSampleUserDTOs() {
        return List.of(
                new UserEntityDTO(new UserEntity("user1", "password1", "user1@example.com")),
                new UserEntityDTO(new UserEntity("user2", "password2", "user2@example.com"))
        );
    }
}