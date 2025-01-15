package com.mindhub.todolist.services.impl;

import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.RegisterUser;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.repositories.UserEntityRepository;
import com.mindhub.todolist.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private TaskEntityRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntityServiceImpl(UserEntityRepository userRepository,
                                 TaskEntityRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public UserEntityDTO getUserDTOById(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
        return new UserEntityDTO(user);
    }

    @Override
    public List<UserEntityDTO> getAllUsersDTO() {
        List<UserEntity> user = userRepository.findAll();
        return user.stream()
                            .map(
                                    userEntity -> new UserEntityDTO(userEntity)
                            ).collect(Collectors.toList());
    }

    @Override
    public UserEntityDTO updateUser(Long id, UserEntityDTO userDetailsDTO) throws UserNotFoundException {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );

        if (userDetailsDTO.getName() != null && !userDetailsDTO.getName().isEmpty()) {
            existingUser.setUsername(userDetailsDTO.getName());
        }
        if (userDetailsDTO.getEmail() != null && !userDetailsDTO.getEmail().isEmpty()) {
            existingUser.setEmail(userDetailsDTO.getEmail());
        }

        if (userDetailsDTO.getTasks() != null && !userDetailsDTO.getTasks().isEmpty()) {
            Set<TaskEntity> updatedTasks = userDetailsDTO.getTasks().stream().map(taskDTO -> {
                TaskEntity taskEntity = taskRepository.findById(taskDTO.getId())
                        .orElseGet(() -> new TaskEntity(taskDTO.getTitle(),
                                                        taskDTO.getDescription(),
                                                        taskDTO.getStatus()));
                taskEntity.setTitle(taskDTO.getTitle());
                taskEntity.setDescription(taskDTO.getDescription());
                taskEntity.setStatus(taskDTO.getStatus());
                return taskEntity;
            }).collect(Collectors.toSet());
            existingUser.setTask(updatedTasks);
        }

        UserEntity updatedUser = userRepository.save(existingUser);
        return new UserEntityDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException { // Admin
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
        userRepository.delete(user);
    }

    @Override
    public void deleteUserByEmail(String email) throws UserNotFoundException { // User
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
    }

    @Override
    public void registerUser(RegisterUser registerUser) {
        if (userRepository.existsByUsername(registerUser.email())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setEmail(registerUser.email());
        user.setPassword(passwordEncoder.encode(registerUser.password()));

        userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
