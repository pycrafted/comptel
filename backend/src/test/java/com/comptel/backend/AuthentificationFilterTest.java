package com.comptel.backend;

import com.comptel.backend.services.JwtService;
import com.comptel.backend.services.UseImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthentificationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UseImpl useImpl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    private AuthentificationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthentificationFilter(jwtService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtService.getAuthUser(request)).thenReturn("testuser");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).getAuthUser(request);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).getAuthUser(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidTokenFormat() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidFormat token");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, never()).getAuthUser(any());
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidTokenWithAdminRole() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer admin-token");
        when(jwtService.getAuthUser(request)).thenReturn("adminuser");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).getAuthUser(request);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WhitespaceInToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer   valid-token  ");
        when(jwtService.getAuthUser(request)).thenReturn("testuser");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).getAuthUser(request);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NullUsername() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtService.getAuthUser(request)).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).getAuthUser(request);
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_EmptyUsername() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtService.getAuthUser(request)).thenReturn("");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService).getAuthUser(request);
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ExceptionInUserLoading() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtService.getAuthUser(request)).thenThrow(new RuntimeException("Token validation failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            filter.doFilterInternal(request, response, filterChain);
        });

        verify(jwtService).getAuthUser(request);
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_MultipleRequests() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid-token");
        when(jwtService.getAuthUser(request)).thenReturn("testuser");

        // Act
        filter.doFilterInternal(request, response, filterChain);
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtService, times(2)).getAuthUser(request);
        verify(securityContext, times(2)).setAuthentication(any(Authentication.class));
        verify(filterChain, times(2)).doFilter(request, response);
    }

    @Test
    void testShouldNotFilter_LoginPath() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/login");

        // Act
        boolean result = filter.shouldNotFilter(request);

        // Assert
        assertTrue(result);
    }

    @Test
    void testShouldNotFilter_HealthPath() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/actuator/health");

        // Act
        boolean result = filter.shouldNotFilter(request);

        // Assert
        assertTrue(result);
    }

    @Test
    void testShouldNotFilter_OtherPath() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/dashboard");

        // Act
        boolean result = filter.shouldNotFilter(request);

        // Assert
        assertFalse(result);
    }
} 