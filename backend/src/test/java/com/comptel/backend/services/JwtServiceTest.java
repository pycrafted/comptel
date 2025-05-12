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
}