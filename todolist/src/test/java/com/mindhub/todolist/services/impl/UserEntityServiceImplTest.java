package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.RegisterUser;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserDTOById_WhenUserIsFound_ShouldReturnUser() throws UserNotFoundException {
        // Arrange
        UserEntity user = new UserEntity("test_user",
                                         "password",
                                         "test@email.com");

        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        UserEntityDTO userDTO = userEntityService.getUserDTOById(user.getId());

        // Assert
        assertEquals(user.getUsername(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void testGetUserDTOById_WhenUserNotFound_ShouldReturnException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userEntityService.getUserDTOById(userId));
    }

    @Test
    void testGetAllUsersDTO_WhenUsersAreFound_ReturnAllUsers() {
        // Arrange
        UserEntity user1 = new UserEntity("user1",
                                          "password",
                                          "user1@email.com");

        UserEntity user2 = new UserEntity("user2",
                                          "password",
                                          "user2@email.com");

        List<UserEntity> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserEntityDTO> userDTOs = userEntityService.getAllUsersDTO();

        // Assert
        assertEquals(users.size(), userDTOs.size());
    }

    @Test
    void testGetAllUsersDTO_WhenNoUsersFound_ReturnEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UserEntityDTO> userDTOs = userEntityService.getAllUsersDTO();

        // Assert
        assertEquals(0, userDTOs.size());
    }

    @Test
    void testUpdateUser_WhenUserIsUpdated_ShouldReturnSuccess() throws UserNotFoundException {
        // Arrange
        UserEntity existingUser = new UserEntity("test_user",
                                                 "password",
                                                 "test@email.com");
        ReflectionTestUtils.setField(existingUser, "id", 1L);

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        UserEntityDTO userDetailsDTO = new UserEntityDTO(existingUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        // Act
        UserEntityDTO updatedUserDTO = userEntityService.updateUser(existingUser.getId(), userDetailsDTO);

        // Assert
        assertEquals(userDetailsDTO.getName(), updatedUserDTO.getName());
        assertEquals(userDetailsDTO.getEmail(), updatedUserDTO.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_WhenUserNotFound_ShouldReturnException() {
        // Arrange
        Long userId = 1L;
        UserEntity existingUser = new UserEntity("test_user",
                "password",
                "test@email.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserEntityDTO userDetailsDTO = new UserEntityDTO(existingUser);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userEntityService.updateUser(userId, userDetailsDTO));
    }

    @Test
    void testDeleteUser_WhenUserIsFound_ShouldDeleteUser() throws UserNotFoundException {
        // Arrange
        UserEntity user = new UserEntity("test_user",
                                         "password",
                                         "test@email.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        userEntityService.deleteUser(user.getId());

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userEntityService.deleteUser(userId));
    }

    @Test
    void testDeleteUserByEmail_WhenUserIsFound_ShouldDeleteUser() throws UserNotFoundException {
        // Arrange
        String email = "test@email.com";
        UserEntity userEntity = new UserEntity("test_user",
                                                "password",
                                                email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // Act
        userEntityService.deleteUserByEmail(email);

        // Assert
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void testDeleteUserByEmail_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        String email = "test@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userEntityService.deleteUserByEmail(email));
    }


    @Test
    void testRegisterUser_WhenNewEmail_ShouldReturnSuccess() {
        // Arrange
        String username = "username";
        String email = "test@email.com";
        String password = "password";
        String hashedPassword = "hashedPassword";

        RegisterUser registerUser = new RegisterUser(username, email, password);

        when(userRepository.existsByUsername(email)).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn(hashedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        // Act
        userEntityService.registerUser(registerUser);

        // Assert
        verify(userRepository).existsByUsername(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUser_WhenEmailAlreadyExists_ShouldThrowException() {
        // Arrange
        String username = "username";
        String email = "test@email.com";
        String password = "password";
        RegisterUser registerUser = new RegisterUser(username, email, password);
        when(userRepository.existsByUsername(email)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userEntityService.registerUser(registerUser));
    }
}