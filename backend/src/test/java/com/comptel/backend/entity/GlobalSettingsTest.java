package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalSettingsTest {

    private GlobalSettings globalSettings;

    @BeforeEach
    void setUp() {
        globalSettings = new GlobalSettings();
    }

    @Test
    void testDefaultValues() {
        assertTrue(globalSettings.isUseDeliveryConfirmation());
        assertTrue(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate());
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        globalSettings.setId(id);
        assertEquals(id, globalSettings.getId());
    }

    @Test
    void testUseDeliveryConfirmationGetterAndSetter() {
        globalSettings.setUseDeliveryConfirmation(false);
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        
        globalSettings.setUseDeliveryConfirmation(true);
        assertTrue(globalSettings.isUseDeliveryConfirmation());
    }

    @Test
    void testUsePartialPaymentGetterAndSetter() {
        globalSettings.setUsePartialPayment(false);
        assertFalse(globalSettings.isUsePartialPayment());
        
        globalSettings.setUsePartialPayment(true);
        assertTrue(globalSettings.isUsePartialPayment());
    }

    @Test
    void testUseAntidateGetterAndSetter() {
        globalSettings.setUseAntidate(false);
        assertFalse(globalSettings.isUseAntidate());
        
        globalSettings.setUseAntidate(true);
        assertTrue(globalSettings.isUseAntidate());
    }

    @Test
    void testIdWithNull() {
        globalSettings.setId(null);
        assertNull(globalSettings.getId());
    }

    @Test
    void testIdWithZero() {
        globalSettings.setId(0L);
        assertEquals(0L, globalSettings.getId());
    }

    @Test
    void testIdWithNegativeValue() {
        globalSettings.setId(-1L);
        assertEquals(-1L, globalSettings.getId());
    }

    @Test
    void testAllSettingsFalse() {
        globalSettings.setUseDeliveryConfirmation(false);
        globalSettings.setUsePartialPayment(false);
        globalSettings.setUseAntidate(false);
        
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        assertFalse(globalSettings.isUsePartialPayment());
        assertFalse(globalSettings.isUseAntidate());
    }

    @Test
    void testAllSettingsTrue() {
        globalSettings.setUseDeliveryConfirmation(true);
        globalSettings.setUsePartialPayment(true);
        globalSettings.setUseAntidate(true);
        
        assertTrue(globalSettings.isUseDeliveryConfirmation());
        assertTrue(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate());
    }

    @Test
    void testMixedSettings() {
        globalSettings.setUseDeliveryConfirmation(true);
        globalSettings.setUsePartialPayment(false);
        globalSettings.setUseAntidate(true);
        
        assertTrue(globalSettings.isUseDeliveryConfirmation());
        assertFalse(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate());
    }

    @Test
    void testToggleSettings() {
        // Start with defaults (all true)
        assertTrue(globalSettings.isUseDeliveryConfirmation());
        assertTrue(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate());
        
        // Toggle each setting
        globalSettings.setUseDeliveryConfirmation(false);
        globalSettings.setUsePartialPayment(false);
        globalSettings.setUseAntidate(false);
        
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        assertFalse(globalSettings.isUsePartialPayment());
        assertFalse(globalSettings.isUseAntidate());
        
        // Toggle back to true
        globalSettings.setUseDeliveryConfirmation(true);
        globalSettings.setUsePartialPayment(true);
        globalSettings.setUseAntidate(true);
        
        assertTrue(globalSettings.isUseDeliveryConfirmation());
        assertTrue(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate());
    }

    @Test
    void testMultipleIdChanges() {
        globalSettings.setId(1L);
        assertEquals(1L, globalSettings.getId());
        
        globalSettings.setId(100L);
        assertEquals(100L, globalSettings.getId());
        
        globalSettings.setId(999999L);
        assertEquals(999999L, globalSettings.getId());
    }

    @Test
    void testSettingsIndependence() {
        // Test that changing one setting doesn't affect others
        globalSettings.setUseDeliveryConfirmation(false);
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        assertTrue(globalSettings.isUsePartialPayment()); // Should still be default
        assertTrue(globalSettings.isUseAntidate()); // Should still be default
        
        globalSettings.setUsePartialPayment(false);
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        assertFalse(globalSettings.isUsePartialPayment());
        assertTrue(globalSettings.isUseAntidate()); // Should still be default
        
        globalSettings.setUseAntidate(false);
        assertFalse(globalSettings.isUseDeliveryConfirmation());
        assertFalse(globalSettings.isUsePartialPayment());
        assertFalse(globalSettings.isUseAntidate());
    }
} 