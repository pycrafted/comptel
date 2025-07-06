package com.comptel.backend.controllers;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.Input;
import com.comptel.backend.entity.Exit;
import com.comptel.backend.repository.InvoiceRepository;
import com.comptel.backend.repository.InputRepository;
import com.comptel.backend.repository.ExitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Tests unitaires avec Mockito
public class DashboardControllerTest {

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InputRepository inputRepository;

    @Mock
    private ExitRepository exitRepository;

    @Mock
    private Invoice mockInvoice;

    @Mock
    private Input mockInput;

    @Mock
    private Exit mockExit;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock invoice
        when(mockInvoice.getId()).thenReturn(1L);
        when(mockInvoice.getTotal()).thenReturn(new BigDecimal("1000.00"));
        when(mockInvoice.getAmountPaid()).thenReturn(new BigDecimal("800.00"));
        when(mockInvoice.getBalance()).thenReturn(new BigDecimal("200.00"));
        when(mockInvoice.isFullyPaid()).thenReturn(false);
        when(mockInvoice.getInvoiceDateTime()).thenReturn(LocalDateTime.now());
        when(mockInvoice.getCustomer()).thenReturn("Test Customer");

        // Setup mock input
        when(mockInput.getId()).thenReturn(1L);
        when(mockInput.getMontants()).thenReturn(new BigDecimal("500.00"));
        when(mockInput.getCreatedAts()).thenReturn(LocalDateTime.now());

        // Setup mock exit
        when(mockExit.getId()).thenReturn(1L);
        when(mockExit.getMontant()).thenReturn(new BigDecimal("100.00"));
        when(mockExit.getCreatedAt()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void testGetDashboardStats_Success() {
        // Arrange
        List<Invoice> allInvoices = Arrays.asList(mockInvoice);
        List<Invoice> todayInvoices = Arrays.asList(mockInvoice);
        List<Invoice> monthInvoices = Arrays.asList(mockInvoice);
        List<Invoice> recentInvoices = Arrays.asList(mockInvoice);
        List<Input> todayInputs = Arrays.asList(mockInput);
        List<Input> monthInputs = Arrays.asList(mockInput);
        List<Exit> todayExits = Arrays.asList(mockExit);
        List<Exit> monthExits = Arrays.asList(mockExit);

        when(invoiceRepository.findAll()).thenReturn(allInvoices);
        when(invoiceRepository.findByInvoiceDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(todayInvoices);
        when(invoiceRepository.findTop5ByOrderByInvoiceDateTimeDesc()).thenReturn(recentInvoices);
        when(inputRepository.findByCreatedAtsBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(todayInputs);
        when(exitRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(todayExits);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboardStats();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        Map<String, Object> stats = response.getBody();
        assertEquals(1, stats.get("totalInvoices"));
        assertEquals("1000.00", stats.get("totalInvoicesAmount"));
        assertEquals("800.00", stats.get("totalInvoicesPaid"));
        assertEquals("200.00", stats.get("totalInvoicesBalance"));
        assertEquals(1, stats.get("todayInvoices"));
        assertEquals("500.00", stats.get("todayInputsAmount"));
        assertEquals("100.00", stats.get("todayExitsAmount"));
    }

    @Test
    public void testGetDashboardStats_EmptyData() {
        // Arrange
        when(invoiceRepository.findAll()).thenReturn(new ArrayList<>());
        when(invoiceRepository.findByInvoiceDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(invoiceRepository.findTop5ByOrderByInvoiceDateTimeDesc()).thenReturn(new ArrayList<>());
        when(inputRepository.findByCreatedAtsBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(exitRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getDashboardStats();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        Map<String, Object> stats = response.getBody();
        assertEquals(0, stats.get("totalInvoices"));
        assertEquals("0", stats.get("totalInvoicesAmount"));
        assertEquals("0", stats.get("totalInvoicesPaid"));
        assertEquals("0", stats.get("totalInvoicesBalance"));
    }

    @Test
    public void testGetStatsByPeriod_Success() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        List<Invoice> periodInvoices = Arrays.asList(mockInvoice);
        List<Input> periodInputs = Arrays.asList(mockInput);
        List<Exit> periodExits = Arrays.asList(mockExit);

        when(invoiceRepository.findByInvoiceDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(periodInvoices);
        when(inputRepository.findByCreatedAtsBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(periodInputs);
        when(exitRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(periodExits);

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getStatsByPeriod(startDate, endDate);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        Map<String, Object> stats = response.getBody();
        assertEquals(1, stats.get("periodInvoices"));
        assertEquals("1000.00", stats.get("periodInvoicesAmount"));
        assertEquals("800.00", stats.get("periodInvoicesPaid"));
        assertEquals("500.00", stats.get("periodInputsAmount"));
        assertEquals("100.00", stats.get("periodExitsAmount"));
    }

    @Test
    public void testGetStatsByPeriod_EmptyData() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(invoiceRepository.findByInvoiceDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(inputRepository.findByCreatedAtsBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        when(exitRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<Map<String, Object>> response = dashboardController.getStatsByPeriod(startDate, endDate);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        Map<String, Object> stats = response.getBody();
        assertEquals(0, stats.get("periodInvoices"));
        assertEquals("0", stats.get("periodInvoicesAmount"));
        assertEquals("0", stats.get("periodInvoicesPaid"));
    }

    @Test
    public void testGetActivityData_Success() {
        // Arrange
        List<Invoice> invoices = Arrays.asList(mockInvoice);
        List<Input> inputs = Arrays.asList(mockInput);
        List<Exit> exits = Arrays.asList(mockExit);

        // Pour chaque jour, on mocke les retours
        when(invoiceRepository.findByInvoiceDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(invoices);
        when(inputRepository.findByCreatedAtsBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(inputs);
        when(exitRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(exits);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = dashboardController.getActivityData();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(7, response.getBody().size());
        for (Map<String, Object> dayData : response.getBody()) {
            assertTrue(dayData.containsKey("date"));
            assertTrue(dayData.containsKey("dayName"));
            assertTrue(dayData.containsKey("invoices"));
            assertTrue(dayData.containsKey("invoicesAmount"));
            assertTrue(dayData.containsKey("inputsAmount"));
            assertTrue(dayData.containsKey("exitsAmount"));
        }
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
    public static class DashboardControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testGetDashboardStats_Integration() throws Exception {
            mockMvc.perform(get("/api/dashboard/stats"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.totalInvoices").exists())
                    .andExpect(jsonPath("$.totalInvoicesAmount").exists())
                    .andExpect(jsonPath("$.todayInvoices").exists());
        }

        @Test
        public void testGetStatsByPeriod_Integration() throws Exception {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now();

            mockMvc.perform(get("/api/dashboard/stats/period")
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$.periodInvoices").exists())
                    .andExpect(jsonPath("$.periodInvoicesAmount").exists());
        }
    }
} 