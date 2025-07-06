package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;
    private Invoice testInvoice;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        testInvoice = new Invoice();
        testInvoice.setId(1L);
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
    }

    @Test
    void testDefaultValues() {
        assertNotNull(payment.getPaymentDate());
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        payment.setId(id);
        assertEquals(id, payment.getId());
    }

    @Test
    void testInvoiceGetterAndSetter() {
        payment.setInvoice(testInvoice);
        assertEquals(testInvoice, payment.getInvoice());
    }

    @Test
    void testAmountGetterAndSetter() {
        BigDecimal amount = new BigDecimal("150.50");
        payment.setAmount(amount);
        assertEquals(amount, payment.getAmount());
    }

    @Test
    void testPaymentDateGetterAndSetter() {
        payment.setPaymentDate(testDate);
        assertEquals(testDate, payment.getPaymentDate());
    }

    @Test
    void testModePaiementGetterAndSetter() {
        Payment.ModePaiement mode = Payment.ModePaiement.CASH;
        payment.setModePaiement(mode);
        assertEquals(mode, payment.getModePaiement());
    }

    @Test
    void testModePaiementEnumValues() {
        Payment.ModePaiement[] modes = Payment.ModePaiement.values();
        assertEquals(3, modes.length);
        
        assertArrayEquals(new Payment.ModePaiement[]{
            Payment.ModePaiement.WAVE,
            Payment.ModePaiement.OM,
            Payment.ModePaiement.CASH
        }, modes);
    }

    @Test
    void testModePaiementValueOf() {
        Payment.ModePaiement wave = Payment.ModePaiement.valueOf("WAVE");
        Payment.ModePaiement om = Payment.ModePaiement.valueOf("OM");
        Payment.ModePaiement cash = Payment.ModePaiement.valueOf("CASH");
        
        assertEquals(Payment.ModePaiement.WAVE, wave);
        assertEquals(Payment.ModePaiement.OM, om);
        assertEquals(Payment.ModePaiement.CASH, cash);
    }

    @Test
    void testModePaiementOrdinal() {
        assertEquals(0, Payment.ModePaiement.WAVE.ordinal());
        assertEquals(1, Payment.ModePaiement.OM.ordinal());
        assertEquals(2, Payment.ModePaiement.CASH.ordinal());
    }

    @Test
    void testAmountWithDifferentPrecisions() {
        BigDecimal amount1 = new BigDecimal("100.123");
        BigDecimal amount2 = new BigDecimal("100.123");
        
        payment.setAmount(amount1);
        assertEquals(amount2, payment.getAmount());
    }

    @Test
    void testAmountWithZero() {
        payment.setAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, payment.getAmount());
    }

    @Test
    void testAmountWithNegativeValue() {
        BigDecimal negativeAmount = new BigDecimal("-50.00");
        payment.setAmount(negativeAmount);
        assertEquals(negativeAmount, payment.getAmount());
    }

    @Test
    void testAmountWithLargeValue() {
        BigDecimal largeAmount = new BigDecimal("999999.99");
        payment.setAmount(largeAmount);
        assertEquals(largeAmount, payment.getAmount());
    }

    @Test
    void testInvoiceWithNull() {
        payment.setInvoice(null);
        assertNull(payment.getInvoice());
    }

    @Test
    void testAmountWithNull() {
        payment.setAmount(null);
        assertNull(payment.getAmount());
    }

    @Test
    void testPaymentDateWithNull() {
        payment.setPaymentDate(null);
        assertNull(payment.getPaymentDate());
    }

    @Test
    void testModePaiementWithNull() {
        payment.setModePaiement(null);
        assertNull(payment.getModePaiement());
    }

    @Test
    void testIdWithNull() {
        payment.setId(null);
        assertNull(payment.getId());
    }

    @Test
    void testIdWithZero() {
        payment.setId(0L);
        assertEquals(0L, payment.getId());
    }

    @Test
    void testIdWithNegativeValue() {
        payment.setId(-1L);
        assertEquals(-1L, payment.getId());
    }

    @Test
    void testMultipleIdChanges() {
        payment.setId(1L);
        assertEquals(1L, payment.getId());
        
        payment.setId(100L);
        assertEquals(100L, payment.getId());
        
        payment.setId(999999L);
        assertEquals(999999L, payment.getId());
    }

    @Test
    void testMultipleAmountChanges() {
        BigDecimal amount1 = new BigDecimal("10.00");
        BigDecimal amount2 = new BigDecimal("25.50");
        BigDecimal amount3 = new BigDecimal("100.75");
        
        payment.setAmount(amount1);
        assertEquals(amount1, payment.getAmount());
        
        payment.setAmount(amount2);
        assertEquals(amount2, payment.getAmount());
        
        payment.setAmount(amount3);
        assertEquals(amount3, payment.getAmount());
    }

    @Test
    void testAllModePaiementValues() {
        for (Payment.ModePaiement mode : Payment.ModePaiement.values()) {
            payment.setModePaiement(mode);
            assertEquals(mode, payment.getModePaiement());
        }
    }

    @Test
    void testInvoiceAndAmountIndependence() {
        // Test that changing invoice doesn't affect amount
        payment.setInvoice(testInvoice);
        BigDecimal amount = new BigDecimal("100.00");
        payment.setAmount(amount);
        
        assertEquals(testInvoice, payment.getInvoice());
        assertEquals(amount, payment.getAmount());
        
        // Change invoice
        Invoice newInvoice = new Invoice();
        newInvoice.setId(2L);
        payment.setInvoice(newInvoice);
        
        assertEquals(newInvoice, payment.getInvoice());
        assertEquals(amount, payment.getAmount()); // Should remain unchanged
    }

    @Test
    void testAmountAndModePaiementIndependence() {
        // Test that changing amount doesn't affect modePaiement
        BigDecimal amount = new BigDecimal("100.00");
        Payment.ModePaiement mode = Payment.ModePaiement.CASH;
        payment.setAmount(amount);
        payment.setModePaiement(mode);
        
        assertEquals(amount, payment.getAmount());
        assertEquals(mode, payment.getModePaiement());
        
        // Change amount
        BigDecimal newAmount = new BigDecimal("200.00");
        payment.setAmount(newAmount);
        
        assertEquals(newAmount, payment.getAmount());
        assertEquals(mode, payment.getModePaiement()); // Should remain unchanged
    }

    @Test
    void testPaymentDateAndModePaiementIndependence() {
        // Test that changing paymentDate doesn't affect modePaiement
        payment.setPaymentDate(testDate);
        Payment.ModePaiement mode = Payment.ModePaiement.OM;
        payment.setModePaiement(mode);
        
        assertEquals(testDate, payment.getPaymentDate());
        assertEquals(mode, payment.getModePaiement());
        
        // Change paymentDate
        LocalDateTime newDate = LocalDateTime.of(2024, 2, 15, 15, 45);
        payment.setPaymentDate(newDate);
        
        assertEquals(newDate, payment.getPaymentDate());
        assertEquals(mode, payment.getModePaiement()); // Should remain unchanged
    }
} 