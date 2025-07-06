package com.comptel.backend.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {

    @Test
    void testInputCreation() {
        // Arrange
        Input input = new Input();
        
        // Act
        input.setId(1L);
        input.setTitres("Test Input");
        input.setMontants(new BigDecimal("100.00"));
        input.setModePaiement(Input.ModePaiement.cash);
        input.setCreatedAts(LocalDateTime.now());
        
        User user = new User("testuser", "password", false);
        input.setSaveBy(user);

        // Assert
        assertEquals(1L, input.getId());
        assertEquals("Test Input", input.getTitres());
        assertEquals(new BigDecimal("100.00"), input.getMontants());
        assertEquals(Input.ModePaiement.cash, input.getModePaiement());
        assertNotNull(input.getCreatedAts());
        assertEquals(user, input.getSaveBy());
    }

    @Test
    void testPrePersistWithNullCreatedAts() {
        // Arrange
        Input input = new Input();
        input.setTitres("Test Input");
        input.setMontants(new BigDecimal("100.00"));
        input.setModePaiement(Input.ModePaiement.om);
        
        // Act
        input.prePersist();

        // Assert
        assertNotNull(input.getCreatedAts());
        assertTrue(input.getCreatedAts().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testPrePersistWithExistingCreatedAts() {
        // Arrange
        Input input = new Input();
        LocalDateTime existingTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        input.setCreatedAts(existingTime);
        input.setTitres("Test Input");
        input.setMontants(new BigDecimal("100.00"));
        input.setModePaiement(Input.ModePaiement.wave);
        
        // Act
        input.prePersist();

        // Assert
        assertEquals(existingTime, input.getCreatedAts());
    }

    @Test
    void testInputWithNullValues() {
        // Arrange
        Input input = new Input();
        
        // Act
        input.setId(null);
        input.setTitres(null);
        input.setMontants(null);
        input.setModePaiement(null);
        input.setCreatedAts(null);
        input.setSaveBy(null);

        // Assert
        assertNull(input.getId());
        assertNull(input.getTitres());
        assertNull(input.getMontants());
        assertNull(input.getModePaiement());
        assertNull(input.getCreatedAts());
        assertNull(input.getSaveBy());
    }

    @Test
    void testInputWithEmptyString() {
        // Arrange
        Input input = new Input();
        
        // Act
        input.setTitres("");

        // Assert
        assertEquals("", input.getTitres());
    }

    @Test
    void testInputWithSpecialCharacters() {
        // Arrange
        Input input = new Input();
        
        // Act
        input.setTitres("Test Input with @#$%^&*()");

        // Assert
        assertEquals("Test Input with @#$%^&*()", input.getTitres());
    }

    @Test
    void testInputWithLargeAmount() {
        // Arrange
        Input input = new Input();
        BigDecimal largeAmount = new BigDecimal("999999.99");
        
        // Act
        input.setMontants(largeAmount);

        // Assert
        assertEquals(largeAmount, input.getMontants());
    }

    @Test
    void testInputWithZeroAmount() {
        // Arrange
        Input input = new Input();
        
        // Act
        input.setMontants(BigDecimal.ZERO);

        // Assert
        assertEquals(BigDecimal.ZERO, input.getMontants());
    }

    @Test
    void testInputWithNegativeAmount() {
        // Arrange
        Input input = new Input();
        BigDecimal negativeAmount = new BigDecimal("-50.00");
        
        // Act
        input.setMontants(negativeAmount);

        // Assert
        assertEquals(negativeAmount, input.getMontants());
    }

    @Test
    void testAllModePaiementValues() {
        // Arrange
        Input input = new Input();
        
        // Act & Assert
        input.setModePaiement(Input.ModePaiement.cash);
        assertEquals(Input.ModePaiement.cash, input.getModePaiement());
        
        input.setModePaiement(Input.ModePaiement.om);
        assertEquals(Input.ModePaiement.om, input.getModePaiement());
        
        input.setModePaiement(Input.ModePaiement.wave);
        assertEquals(Input.ModePaiement.wave, input.getModePaiement());
    }

    @Test
    void testInputWithLongTitle() {
        // Arrange
        Input input = new Input();
        String longTitle = "a".repeat(1000);
        
        // Act
        input.setTitres(longTitle);

        // Assert
        assertEquals(longTitle, input.getTitres());
    }

    @Test
    void testInputWithDifferentUsers() {
        // Arrange
        Input input = new Input();
        User user1 = new User("user1", "pass1", false);
        User user2 = new User("user2", "pass2", true);
        
        // Act & Assert
        input.setSaveBy(user1);
        assertEquals(user1, input.getSaveBy());
        
        input.setSaveBy(user2);
        assertEquals(user2, input.getSaveBy());
    }

    @Test
    void testInputWithSpecificDateTime() {
        // Arrange
        Input input = new Input();
        LocalDateTime specificTime = LocalDateTime.of(2024, 12, 25, 15, 30, 45);
        
        // Act
        input.setCreatedAts(specificTime);

        // Assert
        assertEquals(specificTime, input.getCreatedAts());
    }

    @Test
    void testInputIdChanges() {
        // Arrange
        Input input = new Input();
        
        // Act & Assert
        input.setId(1L);
        assertEquals(1L, input.getId());
        
        input.setId(999L);
        assertEquals(999L, input.getId());
        
        input.setId(null);
        assertNull(input.getId());
    }
} 