package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public class UserEntityDTO {
    private final Long id;
    private final String name;
    private final String email;
    private List<TaskEntityDTO> tasks;

    public UserEntityDTO (UserEntity user) {
        id = user.getId();
        name = user.getUsername();
        email = user.getEmail();
        tasks = user
                .getTask()
                .stream()
                .map( task ->
                        new TaskEntityDTO(task)
                ).toList();
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public List<TaskEntityDTO> getTasks( ){
        return tasks;
    }
}
