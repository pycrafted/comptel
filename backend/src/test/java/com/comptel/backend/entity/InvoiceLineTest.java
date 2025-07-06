package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceLineTest {

    private InvoiceLine invoiceLine;
    private Invoice testInvoice;
    private Service testService;

    @BeforeEach
    void setUp() {
        invoiceLine = new InvoiceLine();
        testInvoice = new Invoice();
        testInvoice.setId(1L);
        testService = new Service();
        testService.setId(1L);
        testService.setDesignation("Test Service");
    }

    @Test
    void testDefaultValues() {
        assertEquals(1, invoiceLine.getQuantite());
        assertEquals(BigDecimal.ZERO, invoiceLine.getPrix());
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        invoiceLine.setId(id);
        assertEquals(id, invoiceLine.getId());
    }

    @Test
    void testInvoiceGetterAndSetter() {
        invoiceLine.setInvoice(testInvoice);
        assertEquals(testInvoice, invoiceLine.getInvoice());
    }

    @Test
    void testServiceGetterAndSetter() {
        invoiceLine.setService(testService);
        assertEquals(testService, invoiceLine.getService());
    }

    @Test
    void testQuantiteGetterAndSetter() {
        int quantite = 5;
        invoiceLine.setQuantite(quantite);
        assertEquals(quantite, invoiceLine.getQuantite());
    }

    @Test
    void testPrixGetterAndSetter() {
        BigDecimal prix = new BigDecimal("150.50");
        invoiceLine.setPrix(prix);
        assertEquals(prix, invoiceLine.getPrix());
    }

    @Test
    void testQuantiteWithZero() {
        invoiceLine.setQuantite(0);
        assertEquals(0, invoiceLine.getQuantite());
    }

    @Test
    void testQuantiteWithNegativeValue() {
        invoiceLine.setQuantite(-5);
        assertEquals(-5, invoiceLine.getQuantite());
    }

    @Test
    void testQuantiteWithLargeValue() {
        invoiceLine.setQuantite(999999);
        assertEquals(999999, invoiceLine.getQuantite());
    }

    @Test
    void testPrixWithDifferentPrecisions() {
        BigDecimal prix1 = new BigDecimal("100.123");
        BigDecimal prix2 = new BigDecimal("100.123");
        
        invoiceLine.setPrix(prix1);
        assertEquals(prix2, invoiceLine.getPrix());
    }

    @Test
    void testPrixWithZero() {
        invoiceLine.setPrix(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, invoiceLine.getPrix());
    }

    @Test
    void testPrixWithNegativeValue() {
        BigDecimal negativePrix = new BigDecimal("-50.00");
        invoiceLine.setPrix(negativePrix);
        assertEquals(negativePrix, invoiceLine.getPrix());
    }

    @Test
    void testPrixWithLargeValue() {
        BigDecimal largePrix = new BigDecimal("999999.99");
        invoiceLine.setPrix(largePrix);
        assertEquals(largePrix, invoiceLine.getPrix());
    }

    @Test
    void testInvoiceWithNull() {
        invoiceLine.setInvoice(null);
        assertNull(invoiceLine.getInvoice());
    }

    @Test
    void testServiceWithNull() {
        invoiceLine.setService(null);
        assertNull(invoiceLine.getService());
    }

    @Test
    void testIdWithNull() {
        invoiceLine.setId(null);
        assertNull(invoiceLine.getId());
    }

    @Test
    void testIdWithZero() {
        invoiceLine.setId(0L);
        assertEquals(0L, invoiceLine.getId());
    }

    @Test
    void testIdWithNegativeValue() {
        invoiceLine.setId(-1L);
        assertEquals(-1L, invoiceLine.getId());
    }

    @Test
    void testMultipleIdChanges() {
        invoiceLine.setId(1L);
        assertEquals(1L, invoiceLine.getId());
        
        invoiceLine.setId(100L);
        assertEquals(100L, invoiceLine.getId());
        
        invoiceLine.setId(999999L);
        assertEquals(999999L, invoiceLine.getId());
    }

    @Test
    void testMultipleQuantiteChanges() {
        invoiceLine.setQuantite(1);
        assertEquals(1, invoiceLine.getQuantite());
        
        invoiceLine.setQuantite(10);
        assertEquals(10, invoiceLine.getQuantite());
        
        invoiceLine.setQuantite(100);
        assertEquals(100, invoiceLine.getQuantite());
    }

    @Test
    void testMultiplePrixChanges() {
        BigDecimal prix1 = new BigDecimal("10.00");
        BigDecimal prix2 = new BigDecimal("25.50");
        BigDecimal prix3 = new BigDecimal("100.75");
        
        invoiceLine.setPrix(prix1);
        assertEquals(prix1, invoiceLine.getPrix());
        
        invoiceLine.setPrix(prix2);
        assertEquals(prix2, invoiceLine.getPrix());
        
        invoiceLine.setPrix(prix3);
        assertEquals(prix3, invoiceLine.getPrix());
    }

    @Test
    void testInvoiceAndServiceIndependence() {
        // Test that changing invoice doesn't affect service
        invoiceLine.setInvoice(testInvoice);
        invoiceLine.setService(testService);
        
        assertEquals(testInvoice, invoiceLine.getInvoice());
        assertEquals(testService, invoiceLine.getService());
        
        // Change invoice
        Invoice newInvoice = new Invoice();
        newInvoice.setId(2L);
        invoiceLine.setInvoice(newInvoice);
        
        assertEquals(newInvoice, invoiceLine.getInvoice());
        assertEquals(testService, invoiceLine.getService()); // Should remain unchanged
    }

    @Test
    void testServiceAndInvoiceIndependence() {
        // Test that changing service doesn't affect invoice
        invoiceLine.setInvoice(testInvoice);
        invoiceLine.setService(testService);
        
        assertEquals(testInvoice, invoiceLine.getInvoice());
        assertEquals(testService, invoiceLine.getService());
        
        // Change service
        Service newService = new Service();
        newService.setId(2L);
        newService.setDesignation("New Service");
        invoiceLine.setService(newService);
        
        assertEquals(testInvoice, invoiceLine.getInvoice()); // Should remain unchanged
        assertEquals(newService, invoiceLine.getService());
    }
} 