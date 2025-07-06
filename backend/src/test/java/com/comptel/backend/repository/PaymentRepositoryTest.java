package com.comptel.backend.repository;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private com.comptel.backend.repository.InvoiceRepository invoiceRepository;

    private Payment testPayment1;
    private Payment testPayment2;
    private Payment testPayment3;
    private Invoice testInvoice;

    @BeforeEach
    void setUp() {
        paymentRepository.deleteAll();
        invoiceRepository.deleteAll();

        testInvoice = new Invoice();
        testInvoice.setCustomer("Test Customer");
        testInvoice.setTelephone("123456789");
        testInvoice.setTotal(new BigDecimal("1000.00"));
        testInvoice.setAmountPaid(BigDecimal.ZERO);
        testInvoice.setBalance(new BigDecimal("1000.00"));
        testInvoice.setDelivered(false);
        testInvoice.setReference(1);
        testInvoice = invoiceRepository.save(testInvoice);

        testPayment1 = new Payment();
        testPayment1.setInvoice(testInvoice);
        testPayment1.setAmount(new BigDecimal("100.00"));
        testPayment1.setModePaiement(Payment.ModePaiement.CASH);
        testPayment1.setPaymentDate(LocalDateTime.of(2024, 1, 15, 10, 0));

        testPayment2 = new Payment();
        testPayment2.setInvoice(testInvoice);
        testPayment2.setAmount(new BigDecimal("250.00"));
        testPayment2.setModePaiement(Payment.ModePaiement.WAVE);
        testPayment2.setPaymentDate(LocalDateTime.of(2024, 1, 16, 14, 30));

        testPayment3 = new Payment();
        testPayment3.setInvoice(testInvoice);
        testPayment3.setAmount(new BigDecimal("500.00"));
        testPayment3.setModePaiement(Payment.ModePaiement.OM);
        testPayment3.setPaymentDate(LocalDateTime.of(2024, 1, 17, 9, 0));
    }

    @Test
    void testSavePayment() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        
        assertNotNull(savedPayment.getId());
        assertEquals(testInvoice, savedPayment.getInvoice());
        assertEquals(new BigDecimal("100.00"), savedPayment.getAmount());
        assertEquals(Payment.ModePaiement.CASH, savedPayment.getModePaiement());
    }

    @Test
    void testFindById() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        Optional<Payment> foundPayment = paymentRepository.findById(savedPayment.getId());
        
        assertTrue(foundPayment.isPresent());
        assertEquals(savedPayment.getId(), foundPayment.get().getId());
        assertEquals(testInvoice, foundPayment.get().getInvoice());
        assertEquals(new BigDecimal("100.00"), foundPayment.get().getAmount());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Payment> foundPayment = paymentRepository.findById(999L);
        assertFalse(foundPayment.isPresent());
    }

    @Test
    void testFindAll() {
        paymentRepository.save(testPayment1);
        paymentRepository.save(testPayment2);
        paymentRepository.save(testPayment3);
        
        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(3, allPayments.size());
    }

    @Test
    void testUpdatePayment() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        savedPayment.setAmount(new BigDecimal("150.00"));
        savedPayment.setModePaiement(Payment.ModePaiement.WAVE);
        
        Payment updatedPayment = paymentRepository.save(savedPayment);
        assertEquals(new BigDecimal("150.00"), updatedPayment.getAmount());
        assertEquals(Payment.ModePaiement.WAVE, updatedPayment.getModePaiement());
    }

    @Test
    void testDeletePayment() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        Long paymentId = savedPayment.getId();
        
        paymentRepository.deleteById(paymentId);
        
        Optional<Payment> foundPayment = paymentRepository.findById(paymentId);
        assertFalse(foundPayment.isPresent());
    }

    @Test
    void testCount() {
        paymentRepository.save(testPayment1);
        paymentRepository.save(testPayment2);
        
        long count = paymentRepository.count();
        assertEquals(2, count);
    }

    @Test
    void testExistsById() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        
        assertTrue(paymentRepository.existsById(savedPayment.getId()));
        assertFalse(paymentRepository.existsById(999L));
    }

    @Test
    void testSaveAll() {
        List<Payment> payments = List.of(testPayment1, testPayment2, testPayment3);
        List<Payment> savedPayments = paymentRepository.saveAll(payments);
        
        assertEquals(3, savedPayments.size());
        assertTrue(savedPayments.stream().allMatch(p -> p.getId() != null));
    }

    @Test
    void testDeleteAll() {
        paymentRepository.save(testPayment1);
        paymentRepository.save(testPayment2);
        
        paymentRepository.deleteAll();
        
        assertEquals(0, paymentRepository.count());
    }

    @Test
    void testDeleteAllById() {
        Payment saved1 = paymentRepository.save(testPayment1);
        Payment saved2 = paymentRepository.save(testPayment2);
        Payment saved3 = paymentRepository.save(testPayment3);
        
        paymentRepository.deleteAllById(List.of(saved1.getId(), saved2.getId()));
        
        assertEquals(1, paymentRepository.count());
        assertTrue(paymentRepository.existsById(saved3.getId()));
    }

    @Test
    void testDefaultValues() {
        Payment payment = new Payment();
        payment.setInvoice(testInvoice);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setModePaiement(Payment.ModePaiement.CASH);
        
        Payment savedPayment = paymentRepository.save(payment);
        assertNotNull(savedPayment.getPaymentDate()); // Should have default date
    }

    @Test
    void testPaymentWithNullValues() {
        Payment paymentWithNulls = new Payment();
        paymentWithNulls.setInvoice(null);
        paymentWithNulls.setAmount(null);
        paymentWithNulls.setModePaiement(null);
        paymentWithNulls.setPaymentDate(null);
        
        Payment savedPayment = paymentRepository.save(paymentWithNulls);
        assertNotNull(savedPayment.getId());
        
        Optional<Payment> foundPayment = paymentRepository.findById(savedPayment.getId());
        assertTrue(foundPayment.isPresent());
        assertNull(foundPayment.get().getInvoice());
        assertNull(foundPayment.get().getAmount());
        assertNull(foundPayment.get().getModePaiement());
        assertNull(foundPayment.get().getPaymentDate());
    }

    @Test
    void testAmountWithZero() {
        testPayment1.setAmount(BigDecimal.ZERO);
        Payment savedPayment = paymentRepository.save(testPayment1);
        assertEquals(BigDecimal.ZERO, savedPayment.getAmount());
    }

    @Test
    void testAmountWithNegativeValue() {
        testPayment1.setAmount(new BigDecimal("-50.00"));
        Payment savedPayment = paymentRepository.save(testPayment1);
        assertEquals(new BigDecimal("-50.00"), savedPayment.getAmount());
    }

    @Test
    void testAmountWithLargeValue() {
        testPayment1.setAmount(new BigDecimal("999999.99"));
        Payment savedPayment = paymentRepository.save(testPayment1);
        assertEquals(new BigDecimal("999999.99"), savedPayment.getAmount());
    }

    @Test
    void testAllModePaiementValues() {
        for (Payment.ModePaiement mode : Payment.ModePaiement.values()) {
            Payment payment = new Payment();
            payment.setInvoice(testInvoice);
            payment.setAmount(new BigDecimal("100.00"));
            payment.setModePaiement(mode);
            payment.setPaymentDate(LocalDateTime.now());
            
            Payment savedPayment = paymentRepository.save(payment);
            assertEquals(mode, savedPayment.getModePaiement());
        }
        
        assertEquals(3, paymentRepository.count()); // 3 modes in the enum
    }

    @Test
    void testPaymentDateWithSpecificDate() {
        LocalDateTime specificDate = LocalDateTime.of(2024, 12, 25, 15, 30, 45);
        testPayment1.setPaymentDate(specificDate);
        
        Payment savedPayment = paymentRepository.save(testPayment1);
        assertEquals(specificDate, savedPayment.getPaymentDate());
    }

    @Test
    void testMultipleUpdates() {
        Payment savedPayment = paymentRepository.save(testPayment1);
        
        // First update
        savedPayment.setAmount(new BigDecimal("200.00"));
        Payment updated1 = paymentRepository.save(savedPayment);
        assertEquals(new BigDecimal("200.00"), updated1.getAmount());
        
        // Second update
        savedPayment.setModePaiement(Payment.ModePaiement.OM);
        Payment updated2 = paymentRepository.save(savedPayment);
        assertEquals(new BigDecimal("200.00"), updated2.getAmount());
        assertEquals(Payment.ModePaiement.OM, updated2.getModePaiement());
        
        // Third update
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        savedPayment.setPaymentDate(newDate);
        savedPayment.setAmount(new BigDecimal("300.00"));
        Payment updated3 = paymentRepository.save(savedPayment);
        assertEquals(new BigDecimal("300.00"), updated3.getAmount());
        assertEquals(Payment.ModePaiement.OM, updated3.getModePaiement());
        assertEquals(newDate, updated3.getPaymentDate());
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
} 