package com.mindhub.todolist;

import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskEntityRepository;
import com.mindhub.todolist.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(UserEntityRepository userEntityRepository,
									  TaskEntityRepository taskEntityRepository) {
		return args -> {
			UserEntity user = new UserEntity("John Doe",
					passwordEncoder.encode("12345678"),
											"johndoe@example.com");
			userEntityRepository.save(user);

			System.out.println(user);

			UserEntity admin = new UserEntity("Jane Doe",
					passwordEncoder.encode("admin123"),
											"janedoe@example.com");
			admin.setRole(RoleType.ADMIN);
			userEntityRepository.save(admin);

			System.out.println(admin);

			TaskEntity taskEntity = new TaskEntity("Task 1",
													"In Progress Task",
													TaskEntity.TaskStatus.IN_PROCESS);
			user.addTask(taskEntity);
			taskEntityRepository.save(taskEntity);
			System.out.println(taskEntity);
		};
	}
}
