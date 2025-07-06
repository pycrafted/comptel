package com.comptel.backend.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {
    @Test
    void updateBalance_shouldSetBalanceCorrectly_whenAmountPaidIsNull() {
        Invoice invoice = new Invoice();
        invoice.setTotal(new BigDecimal("100.00"));
        invoice.setAmountPaid(null); // Cas à couvrir
        invoice.updateBalance();
        assertEquals(new BigDecimal("100.00"), invoice.getBalance());
        assertEquals(BigDecimal.ZERO, invoice.getAmountPaid());
    }

    @Test
    void updateBalance_shouldSetBalanceCorrectly_whenAmountPaidIsNotNull() {
        Invoice invoice = new Invoice();
        invoice.setTotal(new BigDecimal("100.00"));
        invoice.setAmountPaid(new BigDecimal("40.00"));
        invoice.updateBalance();
        assertEquals(new BigDecimal("60.00"), invoice.getBalance());
    }

    @Test
    void isFullyPaid_shouldReturnTrue_whenBalanceIsZero() {
        Invoice invoice = new Invoice();
        invoice.setBalance(BigDecimal.ZERO);
        assertTrue(invoice.isFullyPaid());
    }

    @Test
    void isFullyPaid_shouldReturnFalse_whenBalanceIsNotZero() {
        Invoice invoice = new Invoice();
        invoice.setBalance(new BigDecimal("10.00"));
        assertFalse(invoice.isFullyPaid());
    }

    @Test
    void testGettersAndSetters() {
        Invoice invoice = new Invoice();
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        // Test setters
        invoice.setId(1L);
        invoice.setReference(123);
        invoice.setCustomer("John Doe");
        invoice.setTelephone("123456789");
        invoice.setSaveBy(user);
        invoice.setInvoiceDateTime(now);
        invoice.setTotal(new BigDecimal("150.00"));
        invoice.setAmountPaid(new BigDecimal("50.00"));
        invoice.setBalance(new BigDecimal("100.00"));
        invoice.setDelivered(true);
        invoice.setDeliveredDate(now);

        // Test getters
        assertEquals(1L, invoice.getId());
        assertEquals(123, invoice.getReference());
        assertEquals("John Doe", invoice.getCustomer());
        assertEquals("123456789", invoice.getTelephone());
        assertEquals(user, invoice.getSaveBy());
        assertEquals(now, invoice.getInvoiceDateTime());
        assertEquals(new BigDecimal("150.00"), invoice.getTotal());
        assertEquals(new BigDecimal("50.00"), invoice.getAmountPaid());
        assertEquals(new BigDecimal("100.00"), invoice.getBalance());
        assertTrue(invoice.isDelivered());
        assertEquals(now, invoice.getDeliveredDate());
    }

    @Test
    void testInvoiceServicesGetterAndSetter() {
        Invoice invoice = new Invoice();
        Set<InvoiceLine> services = new HashSet<>();
        
        InvoiceLine line1 = new InvoiceLine();
        line1.setId(1L);
        services.add(line1);
        
        InvoiceLine line2 = new InvoiceLine();
        line2.setId(2L);
        services.add(line2);

        invoice.setInvoiceServices(services);
        assertEquals(services, invoice.getInvoiceServices());
    }

    @Test
    void testPaymentsGetterAndSetter() {
        Invoice invoice = new Invoice();
        Set<Payment> payments = new HashSet<>();
        
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payments.add(payment1);
        
        Payment payment2 = new Payment();
        payment2.setId(2L);
        payments.add(payment2);

        invoice.setPayments(payments);
        assertEquals(payments, invoice.getPayments());
    }

    @Test
    void testNullValues() {
        Invoice invoice = new Invoice();
        
        // Test avec des valeurs null
        invoice.setCustomer(null);
        invoice.setTelephone(null);
        invoice.setSaveBy(null);
        invoice.setInvoiceDateTime(null);
        invoice.setTotal(null);
        invoice.setAmountPaid(null);
        invoice.setBalance(null);
        invoice.setDeliveredDate(null);
        invoice.setInvoiceServices(null);
        invoice.setPayments(null);

        // Vérifier que les getters retournent null
        assertNull(invoice.getCustomer());
        assertNull(invoice.getTelephone());
        assertNull(invoice.getSaveBy());
        assertNull(invoice.getInvoiceDateTime());
        assertNull(invoice.getTotal());
        assertNull(invoice.getAmountPaid());
        assertNull(invoice.getBalance());
        assertNull(invoice.getDeliveredDate());
        assertNull(invoice.getInvoiceServices());
        assertNull(invoice.getPayments());
    }
} 