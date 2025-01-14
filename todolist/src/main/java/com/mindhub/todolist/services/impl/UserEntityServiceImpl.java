package com.mindhub.todolist.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.dtos.UserRegistrationDTO;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserEntityRepository;
import com.mindhub.todolist.services.UserEntityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEntityServiceImpl implements UserEntityService {
    private UserEntityRepository userRepository;
    private ObjectMapper objectMapper;

    public UserEntityServiceImpl(UserEntityRepository userRepository,
                                 ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserEntityDTO getUserDTOById(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
        return objectMapper.convertValue(user, UserEntityDTO.class);
    }

    @Override
    public List<UserEntityDTO> getAllUsersDTO() {
        List<UserEntity> user = userRepository.findAll();
        return user.stream()
                            .map(
                                    userEntity -> objectMapper.convertValue(userEntity,
                                                                            UserEntityDTO.class)
                            ).collect(Collectors.toList());
    }

    @Override
    public UserEntityDTO updateUser(Long id, UserEntityDTO userDetailsDTO) throws UserNotFoundException {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
        existingUser.setUsername(userDetailsDTO.getName());
        existingUser.setEmail(userDetailsDTO.getEmail());
        UserEntity updatedUser = userRepository.save(existingUser);
        return objectMapper.convertValue(existingUser, UserEntityDTO.class);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByUsername(String username) throws UserNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username: " + username)
        );
        userRepository.delete(user);
    }

    @Override
    public void registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(userRegistrationDTO.getPassword());
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
