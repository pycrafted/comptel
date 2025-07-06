package com.comptel.backend.controllers;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.Payment;
import com.comptel.backend.entity.User;
import com.comptel.backend.entity.GlobalSettings;
import com.comptel.backend.services.InvoiceService;
import com.comptel.backend.repository.UserRepository;
import com.comptel.backend.repository.ServiceRepository;
import com.comptel.backend.repository.GlobalSettingsRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
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
    private ServiceRepository serviceRepository;

    @Mock
    private GlobalSettingsRepository globalSettingsRepository;

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
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findAll()).thenReturn(invoices);

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
    public void testDeleteInvoice_Success() {
        // Arrange
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(mockInvoice));
        doNothing().when(mockRepo).delete(mockInvoice);

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.deleteInvoice(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("La facture a été supprimée avec succès.", response.getBody().get("message"));
        verify(mockRepo, times(1)).delete(mockInvoice);
    }

    @Test
    public void testDeleteInvoice_NotFound() {
        // Arrange
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            invoiceController.deleteInvoice(999L);
        });
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
            anyString(), anyString(), anyBoolean(), any(LocalDateTime.class),
            anyList(), anyList(), anyList(),
            any(Payment.ModePaiement.class), any(BigDecimal.class), any(LocalDateTime.class), any(User.class)
        )).thenReturn(mockInvoice);

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.createInvoice(request);

        // Debug: Affiche le code et le corps de la réponse si ce n'est pas 200
        if (response.getStatusCode().value() != 200) {
            System.out.println("Erreur testCreateInvoice_Success : code=" + response.getStatusCode().value());
            System.out.println("Body: " + response.getBody());
        }

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
        ResponseEntity<?> response = invoiceController.patchInvoce(invoiceId, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(true, responseBody.get("success"));
    }

    @Test
    public void testUpdateInvoicePayment_InvalidData() {
        // Arrange
        Long invoiceId = 1L;
        Map<String, Object> request = new HashMap<>();
        // Missing required fields

        // Act
        ResponseEntity<?> response = invoiceController.patchInvoce(invoiceId, request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(false, responseBody.get("success"));
    }

    @Test
    public void testGetInvoicesByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findByInvoiceDateTimeBetween(start, end)).thenReturn(invoices);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getInvoicesByDateRange(start, end);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetAddInvoiceData_Success() {
        // Arrange
        List<com.comptel.backend.entity.Service> services = Arrays.asList();
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        GlobalSettings mockSettings = mock(GlobalSettings.class);
        
        when(serviceRepository.findAll()).thenReturn(services);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findMaxReference()).thenReturn(8000);
        when(globalSettingsRepository.findById(1L)).thenReturn(Optional.of(mockSettings));

        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.getAddInvoiceData();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(8001, response.getBody().get("nextReference"));
        assertEquals(services, response.getBody().get("services"));
    }

    @Test
    public void testGetTotalsByDateRange_Success() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        
        // Mock des données de factures
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(mockRepo.findByInvoiceDateTimeBetween(startDate, endDate)).thenReturn(invoices);
        
        // Act
        ResponseEntity<Map<String, Object>> response = invoiceController.getTotalsByDateRange(startDate, endDate);
        
        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("totalAmount"));
        assertTrue(response.getBody().containsKey("totalPaid"));
        assertTrue(response.getBody().containsKey("totalBalance"));
        assertTrue(response.getBody().containsKey("invoiceCount"));
    }

    @Test
    public void testGetJournalByDate_Success() {
        // Arrange
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);
        
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        when(mockRepo.findByInvoiceDateTimeBetween(startOfDay, endOfDay)).thenReturn(invoices);
        
        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getJournalByDate(date);
        
        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testExtractLongList_WithVariousTypes() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(1); // Integer
        list.add(2L); // Long
        list.add("3"); // String
        request.put("serviceIds", list);
        // Act
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("extractLongList", Map.class, String.class);
        m.setAccessible(true);
        List<Long> result = (List<Long>) m.invoke(invoiceController, request, "serviceIds");
        // Assert
        assertEquals(Arrays.asList(1L, 2L, 3L), result);
    }

    @Test
    public void testExtractLongList_WithInvalidType() throws Exception {
        Map<String, Object> request = new HashMap<>();
        List<Object> list = new ArrayList<>();
        list.add(new Object());
        request.put("serviceIds", list);
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("extractLongList", Map.class, String.class);
        m.setAccessible(true);
        Exception ex = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> m.invoke(invoiceController, request, "serviceIds"));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void testExtractIntegerList_Null() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("quantites", null);
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("extractIntegerList", Map.class, String.class);
        m.setAccessible(true);
        List<Integer> result = (List<Integer>) m.invoke(invoiceController, request, "quantites");
        assertNull(result);
    }

    @Test
    public void testExtractBigDecimalList_Null() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("prixs", null);
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("extractBigDecimalList", Map.class, String.class);
        m.setAccessible(true);
        Exception ex = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> m.invoke(invoiceController, request, "prixs"));
        assertTrue(ex.getCause() instanceof NullPointerException);
    }

    @Test
    public void testParseDateTime_Null() throws Exception {
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("parseDateTime", String.class);
        m.setAccessible(true);
        assertNull(m.invoke(invoiceController, (Object) null));
    }

    @Test
    public void testParsePaymentMode_Null() throws Exception {
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("parsePaymentMode", String.class);
        m.setAccessible(true);
        assertNull(m.invoke(invoiceController, (Object) null));
    }

    @Test
    public void testParseBigDecimal_Null() throws Exception {
        java.lang.reflect.Method m = InvoiceController.class.getDeclaredMethod("parseBigDecimal", String.class);
        m.setAccessible(true);
        assertNull(m.invoke(invoiceController, (Object) null));
    }

    @Test
    public void testGetInvoicesByDateRange_BadRequest() {
        // Arrange
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findByInvoiceDateTimeBetween(any(), any())).thenThrow(new RuntimeException("Erreur DB"));
        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getInvoicesByDateRange(LocalDateTime.now(), LocalDateTime.now());
        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).containsKey("error"));
    }

    @Test
    public void testGetJournalByDate_BadRequest() {
        // Arrange
        com.comptel.backend.repository.InvoiceRepository mockRepo = mock(com.comptel.backend.repository.InvoiceRepository.class);
        when(invoiceService.getInvoiceRepository()).thenReturn(mockRepo);
        when(mockRepo.findByInvoiceDateTimeBetween(any(), any())).thenThrow(new RuntimeException("Erreur DB"));
        // Act
        ResponseEntity<List<Map<String, Object>>> response = invoiceController.getJournalByDate(LocalDate.now());
        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).containsKey("error"));
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