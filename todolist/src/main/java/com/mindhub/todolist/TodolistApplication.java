package com.mindhub.todolist;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.repositories.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserEntityRepository userEntityRepository,
									  TaskEntityRepository taskEntityRepository) {
		return args -> {
			UserEntity userEntity = new UserEntity("John Doe",
													"12345678",
													"johndoe@example.com");
			userEntityRepository.save(userEntity);
			System.out.println(userEntity);

			TaskEntity taskEntity = new TaskEntity("Task 1",
													"In Progress Task",
													TaskEntity.TaskStatus.IN_PROCESS);
			userEntity.addTask(taskEntity);
			taskEntityRepository.save(taskEntity);
			System.out.println(taskEntity);
		};
	}
}
