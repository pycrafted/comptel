package com.comptel.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    public void testGetToken() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        String username = "testuser";

        // Act
        String token = jwtService.getToken(username);

        // Assert
        assertNotNull(token);

        // Utiliser réflexion pour accéder à la clé privée statique
        Field keyField = JwtService.class.getDeclaredField("key");
        keyField.setAccessible(true);
        Object key = keyField.get(null);

        Claims claims = Jwts.parser()
                .setSigningKey((java.security.Key) key)
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    public void testValidateToken() {
        // Arrange
        String username = "testuser";
        String token = jwtService.getToken(username);

        // Act
        boolean isValid = jwtService.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testGetUsernameFromToken() {
        // Arrange
        String username = "testuser";
        String token = jwtService.getToken(username);

        // Act
        String extractedUsername = jwtService.getUsernameFromToken(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertFalse(jwtService.validateToken(invalidToken));
        assertThrows(Exception.class, () -> jwtService.getUsernameFromToken(invalidToken));
    }

    @Test
    public void testGetAuthUserWithValidToken() {
        // Arrange
        String username = "testuser";
        String token = jwtService.getToken(username);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertEquals(username, result);
    }

    @Test
    public void testGetAuthUserWithNoAuthorizationHeader() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAuthUserWithInvalidPrefix() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidPrefix token");

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAuthUserWithInvalidToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalid.token.here");

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAuthUserWithExpiredToken() throws Exception {
        // Arrange
        String username = "testuser";
        String token = jwtService.getToken(username);
        
        // Créer un token expiré en utilisant la réflexion pour modifier la date d'expiration
        Field keyField = JwtService.class.getDeclaredField("key");
        keyField.setAccessible(true);
        Object key = keyField.get(null);

        Claims claims = Jwts.parser()
                .setSigningKey((java.security.Key) key)
                .parseClaimsJws(token)
                .getBody();

        // Créer un nouveau token avec une date d'expiration passée
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 86400000)) // 24h dans le passé
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // 1h dans le passé
                .signWith((java.security.Key) key)
                .compact();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + expiredToken);

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAuthUserWithEmptyToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetAuthUserWithMalformedToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer malformed.token");

        // Act
        String result = jwtService.getAuthUser(request);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetUsernameFromTokenWithExpiredToken() throws Exception {
        // Arrange
        String username = "testuser";
        String token = jwtService.getToken(username);
        
        // Créer un token expiré
        Field keyField = JwtService.class.getDeclaredField("key");
        keyField.setAccessible(true);
        Object key = keyField.get(null);

        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 86400000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000))
                .signWith((java.security.Key) key)
                .compact();

        // Act & Assert
        assertThrows(Exception.class, () -> jwtService.getUsernameFromToken(expiredToken));
    }

    @Test
    public void testGetUsernameFromTokenWithMalformedToken() {
        // Arrange
        String malformedToken = "malformed.token.here";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jwtService.getUsernameFromToken(malformedToken));
    }

    // Tests supplémentaires pour couvrir les lignes manquantes
    @Test
    public void testValidateTokenWithNullToken() {
        // Arrange
        String nullToken = null;

        // Act
        boolean isValid = jwtService.validateToken(nullToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testValidateTokenWithEmptyToken() {
        // Arrange
        String emptyToken = "";

        // Act
        boolean isValid = jwtService.validateToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testValidateTokenWithTokenContainingOnlyDots() {
        // Arrange
        String invalidToken = "..";

        // Act
        boolean isValid = jwtService.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void testValidateTokenWithTokenContainingOneDot() {
        // Arrange
        String invalidToken = "invalid";

        // Act
        boolean isValid = jwtService.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }
}