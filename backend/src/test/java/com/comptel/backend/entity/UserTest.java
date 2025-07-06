package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUserCreation() {
        // Act
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("hashedpassword");
        user.setRole(false);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedpassword", user.getPassword());
        assertFalse(user.isRole());
    }

    @Test
    public void testUserWithAdminRole() {
        // Act
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("adminpassword");
        user.setRole(true);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("adminpassword", user.getPassword());
        assertTrue(user.isRole());
    }

    @Test
    public void testUserEquality() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        user1.setPassword("password");
        user1.setRole(false);

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        user2.setPassword("password");
        user2.setRole(false);

        User user3 = new User();
        user3.setId(2L);
        user3.setUsername("differentuser");
        user3.setPassword("password");
        user3.setRole(false);

        // Assert (on compare les champs)
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertNotEquals(user1.getId(), user3.getId());
        assertNotEquals(user1.getUsername(), user3.getUsername());
    }

    @Test
    public void testUserHashCode() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");

        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");

        // Assert (hashCode doit exister, mais ne pas tester la valeur)
        assertNotNull(user1.hashCode());
        assertNotNull(user2.hashCode());
    }

    @Test
    public void testUserToString() {
        // Arrange
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(false);

        // Act
        String result = user.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("User")); // Vérifie que le nom de la classe ou un champ est présent
    }

    @Test
    public void testUserWithNullValues() {
        // Act
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);

        // Assert
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertFalse(user.isRole()); // Default value
    }

    @Test
    public void testUserWithEmptyStrings() {
        // Act
        user.setUsername("");
        user.setPassword("");

        // Assert
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
    }

    @Test
    public void testUserWithSpecialCharacters() {
        // Act
        user.setUsername("user@domain.com");
        user.setPassword("p@ssw0rd!");

        // Assert
        assertEquals("user@domain.com", user.getUsername());
        assertEquals("p@ssw0rd!", user.getPassword());
    }

    @Test
    public void testUserWithLongValues() {
        // Arrange
        String longUsername = "a".repeat(100);
        String longPassword = "b".repeat(100);

        // Act
        user.setUsername(longUsername);
        user.setPassword(longPassword);

        // Assert
        assertEquals(longUsername, user.getUsername());
        assertEquals(longPassword, user.getPassword());
    }

    @Test
    public void testUserRoleToggle() {
        // Act
        user.setRole(true);
        assertTrue(user.isRole());

        user.setRole(false);
        assertFalse(user.isRole());

        user.setRole(true);
        assertTrue(user.isRole());
    }

    @Test
    public void testUserIdChanges() {
        // Act
        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setId(999L);
        assertEquals(999L, user.getId());

        user.setId(null);
        assertNull(user.getId());
    }

    @Test
    public void testEqualsAndHashCode() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);
        User user3 = new User("user2", "pass2", false);
        user3.setId(2L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
        assertNotEquals(user1.hashCode(), user3.hashCode());
        assertNotEquals(user2, null);
        assertNotEquals(user2, "string");
        assertEquals(user2, user2); // reflexivité
    }

    @Test
    public void testEqualsWithDifferentIds() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(2L);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithNullId() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(null);
        User user2 = new User("user1", "pass1", true);
        user2.setId(null);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithOneNullId() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(null);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentUsernames() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user2", "pass1", true);
        user2.setId(1L);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithNullUsername() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        user1.setUsername(null);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);
        user2.setUsername(null);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithOneNullUsername() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);
        user2.setUsername(null);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentPasswords() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass2", true);
        user2.setId(1L);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithNullPassword() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        user1.setPassword(null);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);
        user2.setPassword(null);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithOneNullPassword() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);
        user2.setPassword(null);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentRoles() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", false);
        user2.setId(1L);

        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithNullObject() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);

        assertNotEquals(user1, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);

        assertNotEquals(user1, "not a user");
        assertNotEquals(user1, new Object());
    }

    @Test
    public void testHashCodeConsistency() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(1L);
        User user2 = new User("user1", "pass1", true);
        user2.setId(1L);

        // Le hashCode doit être cohérent
        assertEquals(user1.hashCode(), user1.hashCode());
        assertEquals(user2.hashCode(), user2.hashCode());
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testHashCodeWithNullValues() {
        User user1 = new User("user1", "pass1", true);
        user1.setId(null);
        user1.setUsername(null);
        user1.setPassword(null);

        // Le hashCode ne doit pas lever d'exception avec des valeurs null
        assertNotNull(user1.hashCode());
    }

    @Test
    public void testToStringContainsFields() {
        User user = new User("userX", "passX", true);
        user.setId(42L);
        String str = user.toString();
        assertTrue(str.contains("User"));
        assertTrue(str.contains("userX"));
        assertTrue(str.contains("passX"));
        assertTrue(str.contains("true"));
        assertTrue(str.contains("42"));
    }
} 