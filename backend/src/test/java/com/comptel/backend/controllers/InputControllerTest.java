package com.comptel.backend.controllers;

import com.comptel.backend.entity.Input;
import com.comptel.backend.entity.User;
import com.comptel.backend.services.InputService;
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
public class InputControllerTest {

    @InjectMocks
    private InputController inputController;

    @Mock
    private InputService inputService;

    @Mock
    private com.comptel.backend.repository.InputRepository mockInputRepository;

    @Mock
    private Input mockInput;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");

        // Setup mock input
        when(mockInput.getId()).thenReturn(1L);
        when(mockInput.getTitres()).thenReturn("Test Input");
        when(mockInput.getMontants()).thenReturn(new BigDecimal("500.00"));
        when(mockInput.getModePaiement()).thenReturn(Input.ModePaiement.cash);
        when(mockInput.getCreatedAts()).thenReturn(LocalDateTime.now());
        when(mockInput.getSaveBy()).thenReturn(mockUser);
    }

    @Test
    public void testGetAllInputs_Success() {
        // Arrange
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputService.getInputRepository()).thenReturn(mockInputRepository);
        when(mockInputRepository.findAll()).thenReturn(inputs);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = inputController.getAllInputs();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        Map<String, Object> inputData = response.getBody().get(0);
        assertEquals(1L, inputData.get("id"));
        assertEquals("Test Input", inputData.get("titres"));
        assertEquals(new BigDecimal("500.00"), inputData.get("montants"));
        assertEquals("cash", inputData.get("modePaiement"));
    }

    @Test
    public void testGetInputById_Success() {
        // Arrange
        when(inputService.findById(1L)).thenReturn(mockInput);

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.getInputById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("Test Input", response.getBody().get("titres"));
    }

    @Test
    public void testGetInputById_NotFound() {
        // Arrange
        when(inputService.findById(999L)).thenThrow(new IllegalArgumentException("Input non trouvé : 999"));

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.getInputById(999L);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertTrue(response.getBody().get("error").toString().contains("Input non trouvé"));
    }

    @Test
    public void testGetInputsByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputService.findByDateRange(start, end)).thenReturn(inputs);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = inputController.getInputsByDateRange(start, end);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetInputsByMode_Success() {
        // Arrange
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputService.findByModePaiement(Input.ModePaiement.cash)).thenReturn(inputs);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = inputController.getInputsByMode(Input.ModePaiement.cash);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testCreateInput_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titres", "New Input");
        request.put("montants", new BigDecimal("750.00"));
        request.put("modePaiement", "cash");
        request.put("saveBy", 1L);

        Input newInput = new Input();
        newInput.setId(2L);
        newInput.setTitres("New Input");
        newInput.setMontants(new BigDecimal("750.00"));
        newInput.setModePaiement(Input.ModePaiement.cash);
        newInput.setSaveBy(mockUser);

        when(inputService.createInput("New Input", new BigDecimal("750.00"), "cash", 1L))
                .thenReturn(newInput);

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.createInput(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        assertEquals("New Input", data.get("titres"));
        assertEquals(new BigDecimal("750.00"), data.get("montants"));
    }

    @Test
    public void testUpdateInput_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titres", "Updated Input");
        request.put("montants", new BigDecimal("1000.00"));
        request.put("modePaiement", "om");

        Input updatedInput = new Input();
        updatedInput.setId(1L);
        updatedInput.setTitres("Updated Input");
        updatedInput.setMontants(new BigDecimal("1000.00"));
        updatedInput.setModePaiement(Input.ModePaiement.om);

        when(inputService.updateInput(1L, "Updated Input", new BigDecimal("1000.00"), "om"))
                .thenReturn(updatedInput);

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.updateInput(1L, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
    }

    @Test
    public void testDeleteInput_Success() {
        // Arrange
        doNothing().when(inputService).deleteInput(1L);

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.deleteInput(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Input supprimé avec succès.", response.getBody().get("message"));
        verify(inputService, times(1)).deleteInput(1L);
    }

    @Test
    public void testGetTotalsByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputService.findByDateRange(start, end)).thenReturn(inputs);

        // Act
        ResponseEntity<Map<String, Object>> response = inputController.getTotalsByDateRange(start, end);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("500.00", response.getBody().get("total"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> byMode = (Map<String, String>) response.getBody().get("byMode");
        assertEquals("500.00", byMode.get("cash"));
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
    public static class InputControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testGetAllInputs_Integration() throws Exception {
            mockMvc.perform(get("/api/inputs"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testCreateInput_Integration() throws Exception {
            Map<String, Object> inputData = new HashMap<>();
            inputData.put("titres", "Integration Test Input");
            inputData.put("montants", 300.00);
            inputData.put("modePaiement", "cash");
            inputData.put("saveBy", 1L);

            mockMvc.perform(post("/api/inputs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputData)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }
    }
} 