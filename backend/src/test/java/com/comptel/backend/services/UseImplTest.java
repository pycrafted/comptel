package com.comptel.backend.services;

import com.comptel.backend.entity.User;
import com.comptel.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UseImplTest {

    @InjectMocks
    private UseImpl useImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");
        when(mockUser.getPassword()).thenReturn("hashedpassword");
        when(mockUser.isRole()).thenReturn(false);
    }

    @Test
    public void testLoadUserByUsername_Success_UserRole() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = useImpl.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("hashedpassword", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_Success_AdminRole() {
        // Arrange
        String username = "adminuser";
        when(mockUser.getUsername()).thenReturn("adminuser");
        when(mockUser.isRole()).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = useImpl.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertTrue(result.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            useImpl.loadUserByUsername(username);
        });

        assertEquals("Utilisateur non trouvé: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserIdByUsername_Success() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        Long result = useImpl.getUserIdByUsername(username);

        // Assert
        assertEquals(1L, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserIdByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            useImpl.getUserIdByUsername(username);
        });

        assertEquals("Utilisateur non trouvé: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_WithDifferentUserData() {
        // Arrange
        String username = "anotheruser";
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername("anotheruser");
        anotherUser.setPassword("anotherpassword");
        anotherUser.setRole(true);
        
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(anotherUser));

        // Act
        UserDetails result = useImpl.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("anotherpassword", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_EmptyUsername() {
        // Arrange
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            useImpl.loadUserByUsername(username);
        });

        assertEquals("Utilisateur non trouvé: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetUserIdByUsername_EmptyUsername() {
        // Arrange
        String username = "";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            useImpl.getUserIdByUsername(username);
        });

        assertEquals("Utilisateur non trouvé: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
} 