package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskEntity;

public class NewTaskEntityDTO {
    private String title;
    private String description;
    private TaskEntity.TaskStatus status;

    public NewTaskEntityDTO(String title, String description, TaskEntity.TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskEntity.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskEntity.TaskStatus status) {
        this.status = status;
    }
}
