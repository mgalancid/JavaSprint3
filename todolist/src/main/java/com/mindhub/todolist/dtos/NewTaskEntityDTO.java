package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskEntity;

public record NewTaskEntityDTO(String title, String description, TaskEntity.TaskStatus status) {
}
