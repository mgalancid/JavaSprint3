package com.mindhub.todolist.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginUser(@NotNull @Email String email, @NotNull @Email String password) {
}
