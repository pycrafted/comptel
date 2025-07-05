package com.comptel.backend.repository;

import com.comptel.backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("hashedpassword");
        testUser.setRole(false);
    }

    @Test
    public void testSaveUser() {
        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("hashedpassword", savedUser.getPassword());
        assertFalse(savedUser.isRole());
    }

    @Test
    public void testFindByUsername_UserExists() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testFindByUsername_UserNotExists() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testFindById_UserExists() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testFindById_UserNotExists() {
        // Act
        Optional<User> foundUser = userRepository.findById(999L);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testSaveUserWithAdminRole() {
        // Arrange
        testUser.setRole(true);

        // Act
        User savedUser = userRepository.save(testUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertTrue(savedUser.isRole());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);
        savedUser.setUsername("updateduser");
        savedUser.setRole(true);

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals("updateduser", updatedUser.getUsername());
        assertTrue(updatedUser.isRole());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        userRepository.delete(savedUser);

        // Assert
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testFindByUsername_CaseSensitive() {
        // Arrange
        User savedUser = entityManager.persistAndFlush(testUser);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("TESTUSER");

        // Assert
        assertFalse(foundUser.isPresent()); // Should be case sensitive
    }

    @Test
    public void testSaveMultipleUsers() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(false);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setRole(true);

        // Act
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        // Assert
        assertNotNull(savedUser1.getId());
        assertNotNull(savedUser2.getId());
        assertEquals("user1", savedUser1.getUsername());
        assertEquals("user2", savedUser2.getUsername());
        assertFalse(savedUser1.isRole());
        assertTrue(savedUser2.isRole());
    }
} 