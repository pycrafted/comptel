package com.comptel.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
}