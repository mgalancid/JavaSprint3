package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskEntity;

public class TaskEntityDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final TaskEntity.TaskStatus status;

    public TaskEntityDTO(TaskEntity task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        status = task.getStatus();
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public TaskEntity.TaskStatus getStatus(){
        return status;
    }
}
