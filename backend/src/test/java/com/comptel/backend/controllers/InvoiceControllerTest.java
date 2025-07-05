package com.comptel.backend.controllers;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.Payment;
import com.comptel.backend.entity.User;
import com.comptel.backend.services.InvoiceService;
import com.comptel.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Tests unitaires avec Mockito
public class InvoiceControllerTest {

    @InjectMocks
    private InvoiceController invoiceController;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Invoice mockInvoice;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");

        // Setup mock invoice
        when(mockInvoice.getId()).thenReturn(1L);
        when(mockInvoice.getReference()).thenReturn(8000);
        when(mockInvoice.getCustomer()).thenReturn("Test Customer");
        when(mockInvoice.getTelephone()).thenReturn("123456789");
        when(mockInvoice.getTotal()).thenReturn(new BigDecimal("1000.00"));
        when(mockInvoice.getAmountPaid()).thenReturn(new BigDecimal("800.00"));
        when(mockInvoice.getBalance()).thenReturn(new BigDecimal("200.00"));
        when(mockInvoice.isDelivered()).thenReturn(true);
        when(mockInvoice.isFullyPaid()).thenReturn(false);
        when(mockInvoice.getInvoiceDateTime()).thenReturn(LocalDateTime.now());
        when(mockInvoice.getSaveBy()).thenReturn(mockUser);
    }

    @Test
    public void testGetAllInvoices_Success() {
        // Arrange
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findAll()).thenReturn(invoices);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getAllInvoices();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        Map<String, Object> invoiceData = response.getBody().get(0);
        assertEquals(1L, invoiceData.get("id"));
        assertEquals(8000, invoiceData.get("reference"));
        assertEquals("Test Customer", invoiceData.get("customer"));
    }

    @Test
    public void testGetInvoiceById_Success() {
        // Arrange
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findById(1L)).thenReturn(Optional.of(mockInvoice));

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.getInvoiceById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("Test Customer", response.getBody().get("customer"));
    }

    @Test
    public void testGetInvoiceById_NotFound() {
        // Arrange
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.getInvoiceById(999L);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertTrue(response.getBody().get("error").toString().contains("Facture non trouvée"));
    }

    @Test
    public void testCreateInvoice_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("customer", "New Customer");
        request.put("telephone", "987654321");
        request.put("delivered", true);
        request.put("invoiceDateTime", LocalDateTime.now().toString());
        request.put("serviceIds", Arrays.asList(1L, 2L));
        request.put("quantites", Arrays.asList(2, 1));
        request.put("prixs", Arrays.asList("100.00", "200.00"));
        request.put("mode_paiement", "CASH");
        request.put("amountPaye", "300.00");
        request.put("paymentDate", LocalDateTime.now().toString());

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(invoiceService.createInvoice(
            eq("New Customer"), eq("987654321"), eq(true), any(LocalDateTime.class),
            anyList(), anyList(), anyList(),
            eq(Payment.ModePaiement.CASH), eq(new BigDecimal("300.00")), any(LocalDateTime.class), eq(mockUser)
        )).thenReturn(mockInvoice);

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.createInvoice(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("1000.00", response.getBody().get("total"));
        assertEquals("200.00", response.getBody().get("balance"));
    }

    @Test
    public void testCreateInvoice_InvalidData() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("customer", "New Customer");
        // Missing required fields

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.createInvoice(request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
    }

    @Test
    public void testUpdateInvoicePayment_Success() {
        // Arrange
        Long invoiceId = 1L;
        Map<String, Object> request = new HashMap<>();
        request.put("amountPaye", "100.00");
        request.put("paymentDate", LocalDateTime.now().toString());
        request.put("mode_paiement", "OM");
        request.put("livrer", true);
        request.put("paiement", true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(invoiceService.updateInvoicePayment(
            eq(invoiceId), eq(new BigDecimal("100.00")), any(LocalDateTime.class),
            eq(Payment.ModePaiement.OM), eq(true), eq(true), eq(mockUser)
        )).thenReturn(mockInvoice);

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.patchInvoce(invoiceId, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
    }

    @Test
    public void testUpdateInvoicePayment_InvalidData() {
        // Arrange
        Long invoiceId = 1L;
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.patchInvoce(invoiceId, request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
    }

    @Test
    public void testGetInvoicesByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findByInvoiceDateTimeBetween(start, end)).thenReturn(invoices);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getInvoicesByDateRange(start, end);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetInvoicesByCustomer_Success() {
        // Arrange
        String customer = "Test Customer";
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findByCustomerContainingIgnoreCase(customer)).thenReturn(invoices);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getInvoicesByCustomer(customer);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetInvoicesByPhone_Success() {
        // Arrange
        String phone = "123456789";
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(invoiceService.getInvoiceRepository()).thenReturn(mock(com.comptel.backend.repository.InvoiceRepository.class));
        when(invoiceService.getInvoiceRepository().findByTelephone(phone)).thenReturn(invoices);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getInvoicesByPhone(phone);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    // Test d'intégration avec MockMvc
    @SpringBootTest
    @AutoConfigureMockMvc
    @ActiveProfiles("test")
    @TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    })
    public static class InvoiceControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testGetAllInvoices_Integration() throws Exception {
            mockMvc.perform(get("/api/invoices"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testCreateInvoice_Integration() throws Exception {
            Map<String, Object> invoiceData = new HashMap<>();
            invoiceData.put("customer", "Integration Test Customer");
            invoiceData.put("telephone", "123456789");
            invoiceData.put("delivered", true);
            invoiceData.put("serviceIds", Arrays.asList(1L));
            invoiceData.put("quantites", Arrays.asList(1));
            invoiceData.put("prixs", Arrays.asList("100.00"));
            invoiceData.put("mode_paiement", "CASH");
            invoiceData.put("amountPaye", "100.00");

            mockMvc.perform(post("/api/invoices")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invoiceData)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.customer").value("Integration Test Customer"));
        }
    }
} 