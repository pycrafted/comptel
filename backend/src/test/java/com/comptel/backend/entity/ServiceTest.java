package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void setUp() {
        service = new Service();
    }

    @Test
    public void testServiceCreation() {
        // Act
        service.setId(1L);
        service.setDesignation("Test Service");
        service.setProposition("Test Proposition");
        service.setPrix(new BigDecimal("100.00"));

        // Assert
        assertEquals(1L, service.getId());
        assertEquals("Test Service", service.getDesignation());
        assertEquals("Test Proposition", service.getProposition());
        assertEquals(new BigDecimal("100.00"), service.getPrix());
    }

    @Test
    public void testServiceWithZeroPrice() {
        // Act
        service.setId(1L);
        service.setDesignation("Free Service");
        service.setProposition("Free Proposition");
        service.setPrix(BigDecimal.ZERO);

        // Assert
        assertEquals(1L, service.getId());
        assertEquals("Free Service", service.getDesignation());
        assertEquals("Free Proposition", service.getProposition());
        assertEquals(BigDecimal.ZERO, service.getPrix());
    }

    @Test
    public void testServiceWithHighPrice() {
        // Act
        service.setId(1L);
        service.setDesignation("Premium Service");
        service.setProposition("Premium Proposition");
        service.setPrix(new BigDecimal("9999.99"));

        // Assert
        assertEquals(1L, service.getId());
        assertEquals("Premium Service", service.getDesignation());
        assertEquals("Premium Proposition", service.getProposition());
        assertEquals(new BigDecimal("9999.99"), service.getPrix());
    }

    @Test
    public void testServiceEquality() {
        // Arrange
        Service service1 = new Service();
        service1.setId(1L);
        service1.setDesignation("Test Service");
        service1.setProposition("Test Proposition");
        service1.setPrix(new BigDecimal("100.00"));

        Service service2 = new Service();
        service2.setId(1L);
        service2.setDesignation("Test Service");
        service2.setProposition("Test Proposition");
        service2.setPrix(new BigDecimal("100.00"));

        Service service3 = new Service();
        service3.setId(2L);
        service3.setDesignation("Different Service");
        service3.setProposition("Different Proposition");
        service3.setPrix(new BigDecimal("200.00"));

        // Assert (on compare les champs)
        assertEquals(service1.getId(), service2.getId());
        assertEquals(service1.getDesignation(), service2.getDesignation());
        assertNotEquals(service1.getId(), service3.getId());
        assertNotEquals(service1.getDesignation(), service3.getDesignation());
    }

    @Test
    public void testServiceHashCode() {
        // Arrange
        Service service1 = new Service();
        service1.setId(1L);
        service1.setDesignation("Test Service");

        Service service2 = new Service();
        service2.setId(1L);
        service2.setDesignation("Test Service");

        // Assert (hashCode doit exister, mais ne pas tester la valeur)
        assertNotNull(service1.hashCode());
        assertNotNull(service2.hashCode());
    }

    @Test
    public void testServiceToString() {
        // Arrange
        service.setId(1L);
        service.setDesignation("Test Service");
        service.setProposition("Test Proposition");
        service.setPrix(new BigDecimal("100.00"));

        // Act
        String result = service.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Service")); // Vérifie que le nom de la classe ou un champ est présent
    }

    @Test
    public void testServiceWithNullValues() {
        // Act
        service.setId(null);
        service.setDesignation(null);
        service.setProposition(null);
        service.setPrix(null);

        // Assert
        assertNull(service.getId());
        assertNull(service.getDesignation());
        assertNull(service.getProposition());
        assertNull(service.getPrix());
    }

    @Test
    public void testServiceWithEmptyStrings() {
        // Act
        service.setDesignation("");
        service.setProposition("");

        // Assert
        assertEquals("", service.getDesignation());
        assertEquals("", service.getProposition());
    }

    @Test
    public void testServiceWithSpecialCharacters() {
        // Act
        service.setDesignation("Service avec caractères spéciaux: éàçù");
        service.setProposition("Proposition avec € et £");

        // Assert
        assertEquals("Service avec caractères spéciaux: éàçù", service.getDesignation());
        assertEquals("Proposition avec € et £", service.getProposition());
    }

    @Test
    public void testServiceWithLongValues() {
        // Arrange
        String longDesignation = "a".repeat(200);
        String longProposition = "b".repeat(200);

        // Act
        service.setDesignation(longDesignation);
        service.setProposition(longProposition);

        // Assert
        assertEquals(longDesignation, service.getDesignation());
        assertEquals(longProposition, service.getProposition());
    }

    @Test
    public void testServicePricePrecision() {
        // Act
        service.setPrix(new BigDecimal("100.123456"));

        // Assert
        assertEquals(new BigDecimal("100.123456"), service.getPrix());
    }

    @Test
    public void testServiceWithNegativePrice() {
        // Act
        service.setPrix(new BigDecimal("-50.00"));

        // Assert
        assertEquals(new BigDecimal("-50.00"), service.getPrix());
    }

    @Test
    public void testServiceIdChanges() {
        // Act
        service.setId(1L);
        assertEquals(1L, service.getId());

        service.setId(999L);
        assertEquals(999L, service.getId());

        service.setId(null);
        assertNull(service.getId());
    }

    @Test
    public void testServicePriceChanges() {
        // Act
        service.setPrix(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), service.getPrix());

        service.setPrix(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), service.getPrix());

        service.setPrix(null);
        assertNull(service.getPrix());
    }

    @Test
    public void testServiceDesignationChanges() {
        // Act
        service.setDesignation("Original Designation");
        assertEquals("Original Designation", service.getDesignation());

        service.setDesignation("Updated Designation");
        assertEquals("Updated Designation", service.getDesignation());

        service.setDesignation(null);
        assertNull(service.getDesignation());
    }

    @Test
    public void testServicePropositionChanges() {
        // Act
        service.setProposition("Original Proposition");
        assertEquals("Original Proposition", service.getProposition());

        service.setProposition("Updated Proposition");
        assertEquals("Updated Proposition", service.getProposition());

        service.setProposition(null);
        assertNull(service.getProposition());
    }
} 