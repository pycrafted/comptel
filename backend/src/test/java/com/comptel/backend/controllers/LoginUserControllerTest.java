package com.comptel.backend.controllers;

import com.comptel.backend.domain.AccountCredentials;
import com.comptel.backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginUserControllerTest {

    @InjectMocks
    private LoginUserController loginUserController;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetToken_Success() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials("testuser", "password");
        String token = "mocked-jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(jwtService.getToken("testuser")).thenReturn(token);

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bearer " + token, response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).getToken("testuser");
    }

    @Test
    public void testGetToken_AuthenticationFailure() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> loginUserController.getToken(credentials));
        verify(jwtService, never()).getToken(anyString());
    }
}