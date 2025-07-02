package com.comptel.backend.controllers;

import com.comptel.backend.domain.AccountCredentials;
import com.comptel.backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.startsWith;

// Tests unitaires avec Mockito
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

    // Test d'intégration avec MockMvc
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class LoginUserControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JwtService jwtService;

        @Test
        public void testLoginSuccess() throws Exception {
            String credentials = "{\"username\": \"admin\", \"password\": \"admin\"}";
            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(credentials))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"))
                    .andExpect(header().string("Authorization", startsWith("Bearer ")));
        }
    }
}