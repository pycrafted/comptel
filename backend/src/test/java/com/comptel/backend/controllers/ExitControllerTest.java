package com.comptel.backend.controllers;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.entity.User;
import com.comptel.backend.services.ExitService;
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
public class ExitControllerTest {

    @InjectMocks
    private ExitController exitController;

    @Mock
    private ExitService exitService;

    @Mock
    private com.comptel.backend.repository.ExitRepository mockExitRepository;

    @Mock
    private Exit mockExit;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");

        // Setup mock exit
        when(mockExit.getId()).thenReturn(1L);
        when(mockExit.getTitre()).thenReturn("Test Exit");
        when(mockExit.getMontant()).thenReturn(new BigDecimal("100.00"));
        when(mockExit.getTypeDepense()).thenReturn(Exit.TypeDepense.RETRAIT);
        when(mockExit.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(mockExit.getSaveBy()).thenReturn(mockUser);
    }

    @Test
    public void testGetAllExits_Success() {
        // Arrange
        List<Exit> exits = Arrays.asList(mockExit);
        when(exitService.getExitRepository()).thenReturn(mockExitRepository);
        when(mockExitRepository.findAll()).thenReturn(exits);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = exitController.getAllExits();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        Map<String, Object> exitData = response.getBody().get(0);
        assertEquals(1L, exitData.get("id"));
        assertEquals("Test Exit", exitData.get("titre"));
        assertEquals(new BigDecimal("100.00"), exitData.get("montant"));
        assertEquals(Exit.TypeDepense.RETRAIT, exitData.get("typeDepense"));
    }

    @Test
    public void testGetExitById_Success() {
        // Arrange
        when(exitService.findById(1L)).thenReturn(mockExit);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.getExitById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("Test Exit", response.getBody().get("titre"));
    }

    @Test
    public void testGetExitById_NotFound() {
        // Arrange
        when(exitService.findById(999L)).thenThrow(new IllegalArgumentException("Exit non trouvé : 999"));

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.getExitById(999L);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertTrue(response.getBody().get("error").toString().contains("Exit non trouvé"));
    }

    @Test
    public void testGetExitsByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Exit> exits = Arrays.asList(mockExit);
        when(exitService.findByDateRange(start, end)).thenReturn(exits);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = exitController.getExitsByDateRange(start, end);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetExitsByType_Success() {
        // Arrange
        List<Exit> exits = Arrays.asList(mockExit);
        when(exitService.findByType(Exit.TypeDepense.RETRAIT)).thenReturn(exits);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = exitController.getExitsByType(Exit.TypeDepense.RETRAIT);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetExitsByType_EmptyList() {
        // Arrange
        when(exitService.findByType(Exit.TypeDepense.REPARATION)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<Map<String, Object>>> response = exitController.getExitsByType(Exit.TypeDepense.REPARATION);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetExitsByType_MultipleExits() {
        // Arrange
        Exit exit1 = new Exit();
        exit1.setId(1L);
        exit1.setTitre("Exit 1");
        exit1.setMontant(new BigDecimal("100.00"));
        exit1.setTypeDepense(Exit.TypeDepense.RETRAIT);
        exit1.setSaveBy(mockUser);

        Exit exit2 = new Exit();
        exit2.setId(2L);
        exit2.setTitre("Exit 2");
        exit2.setMontant(new BigDecimal("200.00"));
        exit2.setTypeDepense(Exit.TypeDepense.RETRAIT);
        exit2.setSaveBy(mockUser);

        List<Exit> exits = Arrays.asList(exit1, exit2);
        when(exitService.findByType(Exit.TypeDepense.RETRAIT)).thenReturn(exits);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = exitController.getExitsByType(Exit.TypeDepense.RETRAIT);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(new BigDecimal("100.00"), response.getBody().get(0).get("montant"));
        assertEquals(new BigDecimal("200.00"), response.getBody().get(1).get("montant"));
    }

    @Test
    public void testCreateExit_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titre", "New Exit");
        request.put("montant", new BigDecimal("150.00"));
        request.put("typeDepense", "RETRAIT");
        request.put("userId", 1L);

        Exit newExit = new Exit();
        newExit.setId(2L);
        newExit.setTitre("New Exit");
        newExit.setMontant(new BigDecimal("150.00"));
        newExit.setTypeDepense(Exit.TypeDepense.RETRAIT);
        newExit.setSaveBy(mockUser);

        when(exitService.createExit("New Exit", new BigDecimal("150.00"), Exit.TypeDepense.RETRAIT, 1L))
                .thenReturn(newExit);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.createExit(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        assertEquals("New Exit", data.get("titre"));
        assertEquals(new BigDecimal("150.00"), data.get("montant"));
    }

    @Test
    public void testCreateExit_Exception() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titre", "New Exit");
        request.put("montant", "invalid_amount");
        request.put("typeDepense", "RETRAIT");
        request.put("userId", 1L);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.createExit(request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertNotNull(response.getBody().get("error"));
    }

    @Test
    public void testUpdateExit_Exception() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titre", "Updated Exit");
        request.put("montant", "invalid_amount");
        request.put("typeDepense", "RETRAIT");

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.updateExit(1L, request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertNotNull(response.getBody().get("error"));
    }

    @Test
    public void testUpdateExit_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("titre", "Updated Exit");
        request.put("montant", new BigDecimal("200.00"));
        request.put("typeDepense", "REPARATION");

        Exit updatedExit = new Exit();
        updatedExit.setId(1L);
        updatedExit.setTitre("Updated Exit");
        updatedExit.setMontant(new BigDecimal("200.00"));
        updatedExit.setTypeDepense(Exit.TypeDepense.REPARATION);

        when(exitService.updateExit(1L, "Updated Exit", new BigDecimal("200.00"), Exit.TypeDepense.REPARATION))
                .thenReturn(updatedExit);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.updateExit(1L, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
    }

    @Test
    public void testDeleteExit_Success() {
        // Arrange
        doNothing().when(exitService).deleteExit(1L);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.deleteExit(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Sortie supprimée avec succès", response.getBody().get("message"));
        verify(exitService, times(1)).deleteExit(1L);
    }

    @Test
    public void testDeleteExit_NotFound() {
        // Arrange
        doThrow(new IllegalArgumentException("Exit non trouvé : 999")).when(exitService).deleteExit(999L);

        // Act
        ResponseEntity<Map<String, Object>> response = exitController.deleteExit(999L);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertTrue(response.getBody().get("error").toString().contains("Exit non trouvé"));
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
    public static class ExitControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testGetAllExits_Integration() throws Exception {
            mockMvc.perform(get("/api/exits"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testCreateExit_Integration() throws Exception {
            Map<String, Object> exitData = new HashMap<>();
            exitData.put("titre", "Integration Test Exit");
            exitData.put("montant", 300.00);
            exitData.put("typeDepense", "RETRAIT");
            exitData.put("userId", 1L);

            mockMvc.perform(post("/api/exits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(exitData)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.titre").value("Integration Test Exit"));
        }

        @Test
        public void testGetExitsByType_Integration() throws Exception {
            mockMvc.perform(get("/api/exits/by-type/RETRAIT"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testGetExitsByDateRange_Integration() throws Exception {
            LocalDateTime start = LocalDateTime.now().minusDays(7);
            LocalDateTime end = LocalDateTime.now();

            mockMvc.perform(get("/api/exits/by-date-range")
                    .param("start", start.toString())
                    .param("end", end.toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }
} 