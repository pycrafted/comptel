package com.comptel.backend.repository;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.InvoiceLine;
import com.comptel.backend.entity.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InvoiceLineRepositoryTest {

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    @Autowired
    private com.comptel.backend.repository.InvoiceRepository invoiceRepository;

    @Autowired
    private com.comptel.backend.repository.ServiceRepository serviceRepository;

    private InvoiceLine testInvoiceLine1;
    private InvoiceLine testInvoiceLine2;
    private InvoiceLine testInvoiceLine3;
    private Invoice testInvoice;
    private Service testService;

    @BeforeEach
    void setUp() {
        invoiceLineRepository.deleteAll();
        invoiceRepository.deleteAll();
        serviceRepository.deleteAll();

        testInvoice = new Invoice();
        testInvoice.setCustomer("Test Customer");
        testInvoice.setTelephone("123456789");
        testInvoice.setTotal(new BigDecimal("1000.00"));
        testInvoice.setAmountPaid(BigDecimal.ZERO);
        testInvoice.setBalance(new BigDecimal("1000.00"));
        testInvoice.setDelivered(false);
        testInvoice.setReference(1);
        testInvoice = invoiceRepository.save(testInvoice);

        testService = new Service();
        testService.setDesignation("Test Service");
        testService.setPrix(new BigDecimal("100.00"));
        testService.setProposition("Test Proposition");
        testService = serviceRepository.save(testService);

        testInvoiceLine1 = new InvoiceLine();
        testInvoiceLine1.setInvoice(testInvoice);
        testInvoiceLine1.setService(testService);
        testInvoiceLine1.setQuantite(2);
        testInvoiceLine1.setPrix(new BigDecimal("100.00"));

        testInvoiceLine2 = new InvoiceLine();
        testInvoiceLine2.setInvoice(testInvoice);
        testInvoiceLine2.setService(testService);
        testInvoiceLine2.setQuantite(1);
        testInvoiceLine2.setPrix(new BigDecimal("150.00"));

        testInvoiceLine3 = new InvoiceLine();
        testInvoiceLine3.setInvoice(testInvoice);
        testInvoiceLine3.setService(testService);
        testInvoiceLine3.setQuantite(3);
        testInvoiceLine3.setPrix(new BigDecimal("75.00"));
    }

    @Test
    void testSaveInvoiceLine() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        
        assertNotNull(savedInvoiceLine.getId());
        assertEquals(testInvoice, savedInvoiceLine.getInvoice());
        assertEquals(testService, savedInvoiceLine.getService());
        assertEquals(2, savedInvoiceLine.getQuantite());
        assertEquals(new BigDecimal("100.00"), savedInvoiceLine.getPrix());
    }

    @Test
    void testFindById() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        Optional<InvoiceLine> foundInvoiceLine = invoiceLineRepository.findById(savedInvoiceLine.getId());
        
        assertTrue(foundInvoiceLine.isPresent());
        assertEquals(savedInvoiceLine.getId(), foundInvoiceLine.get().getId());
        assertEquals(testInvoice, foundInvoiceLine.get().getInvoice());
        assertEquals(testService, foundInvoiceLine.get().getService());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<InvoiceLine> foundInvoiceLine = invoiceLineRepository.findById(999L);
        assertFalse(foundInvoiceLine.isPresent());
    }

    @Test
    void testFindAll() {
        invoiceLineRepository.save(testInvoiceLine1);
        invoiceLineRepository.save(testInvoiceLine2);
        invoiceLineRepository.save(testInvoiceLine3);
        
        List<InvoiceLine> allInvoiceLines = invoiceLineRepository.findAll();
        assertEquals(3, allInvoiceLines.size());
    }

    @Test
    void testUpdateInvoiceLine() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        savedInvoiceLine.setQuantite(5);
        savedInvoiceLine.setPrix(new BigDecimal("200.00"));
        
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.save(savedInvoiceLine);
        assertEquals(5, updatedInvoiceLine.getQuantite());
        assertEquals(new BigDecimal("200.00"), updatedInvoiceLine.getPrix());
    }

    @Test
    void testDeleteInvoiceLine() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        Long invoiceLineId = savedInvoiceLine.getId();
        
        invoiceLineRepository.deleteById(invoiceLineId);
        
        Optional<InvoiceLine> foundInvoiceLine = invoiceLineRepository.findById(invoiceLineId);
        assertFalse(foundInvoiceLine.isPresent());
    }

    @Test
    void testCount() {
        invoiceLineRepository.save(testInvoiceLine1);
        invoiceLineRepository.save(testInvoiceLine2);
        
        long count = invoiceLineRepository.count();
        assertEquals(2, count);
    }

    @Test
    void testExistsById() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        
        assertTrue(invoiceLineRepository.existsById(savedInvoiceLine.getId()));
        assertFalse(invoiceLineRepository.existsById(999L));
    }

    @Test
    void testSaveAll() {
        List<InvoiceLine> invoiceLines = List.of(testInvoiceLine1, testInvoiceLine2, testInvoiceLine3);
        List<InvoiceLine> savedInvoiceLines = invoiceLineRepository.saveAll(invoiceLines);
        
        assertEquals(3, savedInvoiceLines.size());
        assertTrue(savedInvoiceLines.stream().allMatch(il -> il.getId() != null));
    }

    @Test
    void testDeleteAll() {
        invoiceLineRepository.save(testInvoiceLine1);
        invoiceLineRepository.save(testInvoiceLine2);
        
        invoiceLineRepository.deleteAll();
        
        assertEquals(0, invoiceLineRepository.count());
    }

    @Test
    void testDeleteAllById() {
        InvoiceLine saved1 = invoiceLineRepository.save(testInvoiceLine1);
        InvoiceLine saved2 = invoiceLineRepository.save(testInvoiceLine2);
        InvoiceLine saved3 = invoiceLineRepository.save(testInvoiceLine3);
        
        invoiceLineRepository.deleteAllById(List.of(saved1.getId(), saved2.getId()));
        
        assertEquals(1, invoiceLineRepository.count());
        assertTrue(invoiceLineRepository.existsById(saved3.getId()));
    }

    @Test
    void testDefaultValues() {
        InvoiceLine invoiceLine = new InvoiceLine();
        invoiceLine.setInvoice(testInvoice);
        invoiceLine.setService(testService);
        
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(invoiceLine);
        assertEquals(1, savedInvoiceLine.getQuantite()); // Default value
        assertEquals(BigDecimal.ZERO, savedInvoiceLine.getPrix()); // Default value
    }

    @Test
    void testInvoiceLineWithNullValues() {
        InvoiceLine invoiceLineWithNulls = new InvoiceLine();
        invoiceLineWithNulls.setInvoice(null);
        invoiceLineWithNulls.setService(null);
        invoiceLineWithNulls.setQuantite(0);
        invoiceLineWithNulls.setPrix(null);
        
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(invoiceLineWithNulls);
        assertNotNull(savedInvoiceLine.getId());
        
        Optional<InvoiceLine> foundInvoiceLine = invoiceLineRepository.findById(savedInvoiceLine.getId());
        assertTrue(foundInvoiceLine.isPresent());
        assertNull(foundInvoiceLine.get().getInvoice());
        assertNull(foundInvoiceLine.get().getService());
        assertEquals(0, foundInvoiceLine.get().getQuantite());
        assertNull(foundInvoiceLine.get().getPrix());
    }

    @Test
    void testQuantiteWithZero() {
        testInvoiceLine1.setQuantite(0);
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(0, savedInvoiceLine.getQuantite());
    }

    @Test
    void testQuantiteWithNegativeValue() {
        testInvoiceLine1.setQuantite(-5);
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(-5, savedInvoiceLine.getQuantite());
    }

    @Test
    void testQuantiteWithLargeValue() {
        testInvoiceLine1.setQuantite(999999);
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(999999, savedInvoiceLine.getQuantite());
    }

    @Test
    void testPrixWithZero() {
        testInvoiceLine1.setPrix(BigDecimal.ZERO);
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(BigDecimal.ZERO, savedInvoiceLine.getPrix());
    }

    @Test
    void testPrixWithNegativeValue() {
        testInvoiceLine1.setPrix(new BigDecimal("-50.00"));
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(new BigDecimal("-50.00"), savedInvoiceLine.getPrix());
    }

    @Test
    void testPrixWithLargeValue() {
        testInvoiceLine1.setPrix(new BigDecimal("999999.99"));
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        assertEquals(new BigDecimal("999999.99"), savedInvoiceLine.getPrix());
    }

    @Test
    void testMultipleUpdates() {
        InvoiceLine savedInvoiceLine = invoiceLineRepository.save(testInvoiceLine1);
        
        // First update
        savedInvoiceLine.setQuantite(10);
        InvoiceLine updated1 = invoiceLineRepository.save(savedInvoiceLine);
        assertEquals(10, updated1.getQuantite());
        
        // Second update
        savedInvoiceLine.setPrix(new BigDecimal("300.00"));
        InvoiceLine updated2 = invoiceLineRepository.save(savedInvoiceLine);
        assertEquals(10, updated2.getQuantite());
        assertEquals(new BigDecimal("300.00"), updated2.getPrix());
        
        // Third update
        savedInvoiceLine.setQuantite(15);
        savedInvoiceLine.setPrix(new BigDecimal("400.00"));
        InvoiceLine updated3 = invoiceLineRepository.save(savedInvoiceLine);
        assertEquals(15, updated3.getQuantite());
        assertEquals(new BigDecimal("400.00"), updated3.getPrix());
    }


} 