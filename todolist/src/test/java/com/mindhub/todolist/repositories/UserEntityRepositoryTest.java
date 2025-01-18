package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserEntityRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserEntityRepository userRepository;

    @Test
    void testFindByUsername_WhenGivenUsername_ShouldReturnTrue() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        entityManager.persistAndFlush(user);

        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername("johndoe");

        // Assert
        assertTrue(foundUser.isPresent(), "Username was found");
    }

    @Test
    void testFindByUsername_WhenGivenIncorrectUsername_ShouldReturnFalse() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        entityManager.persistAndFlush(user);

        String nonExistentUser = "nonexistent";

        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername(nonExistentUser);

        // Assert
        assertFalse(foundUser.isPresent(), "Username is incorrect");
    }

    @Test
    void testFindByEmail_WhenGivenEmail_ShouldReturnTrue() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        entityManager.persistAndFlush(user);

        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("johndoe@example.com");

        // Assert
        assertTrue(foundUser.isPresent(), "Username was found");
    }

    @Test
    void testFindByEmail_WhenGivenIncorrectEmail_ShouldReturnFalse() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        entityManager.persistAndFlush(user);

        String nonExistentEmail = "nonexistent@email.com";

        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail(nonExistentEmail);

        // Assert
        assertFalse(foundUser.isPresent(), "Username was found");
    }

    @Test
    void testExistsByEmail_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        userRepository.saveAndFlush(user);

        // Act
        boolean exists = userRepository.existsByEmail(user.getEmail());

        // Assert
        assertTrue(exists, "User with email should exist");
    }

    @Test
    void testExistsByEmail_WhenUserDoesntExist_ShouldReturnFalse() {
        // Arrange
        String nonExistentEmail = "nonexistent@example.com";

        // Act
        boolean exists = userRepository.existsByEmail(nonExistentEmail);

        // Assert
        assertFalse(exists, "User with email should not exist");
    }

    @Test
    void testExistsByUsername_WhenUserExists_ShouldReturnTrue() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        userRepository.saveAndFlush(user);

        // Act
        boolean exists = userRepository.existsByUsername(user.getUsername());

        // Assert
        assertTrue(exists, "User with username should exist");
    }

    @Test
    void testExistsByUsername_WhenUserDoesntExist_ShouldReturnFalse() {
        // Arrange
        String nonExistentUsername = "nonexistentuser";

        // Act
        boolean exists = userRepository.existsByUsername(nonExistentUsername);

        // Assert
        assertFalse(exists, "User with username should not exist");
    }

    @Test
    void testFindByRole() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        user.setRole(RoleType.USER);
        userRepository.saveAndFlush(user);

        // Act
        List<UserEntity> foundUsers = userRepository.findByRole(RoleType.USER);

        // Assert
        assertFalse(foundUsers.isEmpty(), "User with role should be found");
    }
}
