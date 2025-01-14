package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.UserEntityDTO;
import com.mindhub.todolist.dtos.UserRegistrationDTO;
import com.mindhub.todolist.exceptions.UserAlreadyExistsException;
import com.mindhub.todolist.exceptions.UserNotFoundException;
import com.mindhub.todolist.models.RegisterUser;

import java.util.List;

public interface UserEntityService {
    UserEntityDTO getUserDTOById(Long id) throws UserNotFoundException;
    List<UserEntityDTO> getAllUsersDTO();
    UserEntityDTO updateUser(Long id, UserEntityDTO userDetailsDTO) throws UserNotFoundException;
    void deleteUserById(Long id);
    void deleteUserByUsername(String username) throws UserNotFoundException;
    void registerUser(RegisterUser registerUser);
}
