package com.comptel.backend.controllers;

import com.comptel.backend.domain.AccountCredentials;
import com.comptel.backend.services.JwtService;
import com.comptel.backend.services.UseImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

    @Mock
    private UseImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        
        // Mock userService methods
        org.springframework.security.core.userdetails.UserDetails userDetails = 
            org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("USER")
                .build();
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userService.getUserIdByUsername("testuser")).thenReturn(1L);

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).getToken("testuser");
    }

    @Test
    public void testGetToken_AuthenticationFailure() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        verify(jwtService, never()).getToken(anyString());
    }

    @Test
    public void testGetToken_NullCredentials() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials(null, null);

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Identifiants invalides"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    public void testGetToken_NullUsername() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials(null, "password");

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Identifiants invalides"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    public void testGetToken_NullPassword() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials("testuser", null);

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Identifiants invalides"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    public void testGetToken_GeneralException() {
        // Arrange
        AccountCredentials credentials = new AccountCredentials("testuser", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = loginUserController.getToken(credentials);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("Erreur serveur"));
        verify(jwtService, never()).getToken(anyString());
    }

    @Test
    public void testVerifyToken_ValidToken() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer valid-token");
        
        when(jwtService.validateToken("valid-token")).thenReturn(true);
        when(jwtService.getUsernameFromToken("valid-token")).thenReturn("testuser");
        
        org.springframework.security.core.userdetails.UserDetails userDetails = 
            org.springframework.security.core.userdetails.User.builder()
                .username("testuser")
                .password("password")
                .authorities("USER")
                .build();
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userService.getUserIdByUsername("testuser")).thenReturn(1L);

        // Act
        ResponseEntity<?> response = loginUserController.verifyToken(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(jwtService, times(1)).validateToken("valid-token");
        verify(jwtService, times(1)).getUsernameFromToken("valid-token");
    }

    @Test
    public void testVerifyToken_InvalidToken() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");
        
        when(jwtService.validateToken("invalid-token")).thenReturn(false);

        // Act
        ResponseEntity<?> response = loginUserController.verifyToken(request);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        verify(jwtService, times(1)).validateToken("invalid-token");
        verify(jwtService, never()).getUsernameFromToken(anyString());
    }

    @Test
    public void testVerifyToken_NoToken() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act
        ResponseEntity<?> response = loginUserController.verifyToken(request);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        verify(jwtService, never()).validateToken(anyString());
    }

    @Test
    public void testVerifyToken_NoBearerPrefix() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "valid-token");

        // Act
        ResponseEntity<?> response = loginUserController.verifyToken(request);

        // Assert
        assertEquals(401, response.getStatusCode().value());
        verify(jwtService, never()).validateToken(anyString());
    }

    // Test d'intégration avec MockMvc
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    })
    public static class LoginUserControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JwtService jwtService;

        @Test
        public void testLoginSuccess() throws Exception {
            String credentials = "{\"username\": \"admin\", \"password\": \"admin\"}";
            mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(credentials))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.username").value("admin"));
        }

        @Test
        public void testLoginFailure() throws Exception {
            String credentials = "{\"username\": \"admin\", \"password\": \"wrongpassword\"}";
            mockMvc.perform(post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(credentials))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void testVerifyTokenEndpoint() throws Exception {
            mockMvc.perform(get("/api/verify-token")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
                    .andExpect(status().isUnauthorized());
        }
    }
}