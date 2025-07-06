package com.comptel.backend.controllers;

import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;
import com.comptel.backend.services.ServiceF;
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
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Tests unitaires avec Mockito
public class ServiceControllerTest {

    @InjectMocks
    private ServiceController serviceController;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceF serviceF;

    @Mock
    private Service mockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock service
        when(mockService.getId()).thenReturn(1L);
        when(mockService.getDesignation()).thenReturn("Test Service");
        when(mockService.getProposition()).thenReturn("Test Proposition");
        when(mockService.getPrix()).thenReturn(new BigDecimal("100.00"));
    }

    @Test
    public void testGetAllServices_Success() {
        // Arrange
        List<Service> services = Arrays.asList(mockService);
        when(serviceF.getServiceRepository()).thenReturn(serviceRepository);
        when(serviceRepository.findAll()).thenReturn(services);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = serviceController.getAllService();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        
        Map<String, Object> serviceData = response.getBody().get(0);
        assertEquals(1L, serviceData.get("id"));
        assertEquals("Test Service", serviceData.get("designation"));
        assertEquals("Test Proposition", serviceData.get("proposition"));
        assertEquals(new BigDecimal("100.00"), serviceData.get("prix"));
    }

    @Test
    public void testGetServiceById_Success() {
        // Arrange
        when(serviceF.findById(1L)).thenReturn(mockService);

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.getServiceById(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().get("id"));
        assertEquals("Test Service", response.getBody().get("designation"));
    }

    @Test
    public void testGetServiceById_NotFound() {
        // Arrange
        when(serviceF.findById(999L)).thenThrow(new IllegalArgumentException("Service non trouvé : 999"));

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.getServiceById(999L);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().get("success"));
        assertTrue(response.getBody().get("error").toString().contains("Service non trouvé"));
    }

    @Test
    public void testCreateService_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("designation", "New Service");
        request.put("prix", new BigDecimal("150.00"));
        request.put("proposition", "New Proposition");

        Service newService = new Service();
        newService.setId(2L);
        newService.setDesignation("New Service");
        newService.setPrix(new BigDecimal("150.00"));
        newService.setProposition("New Proposition");

        when(serviceF.CreateServiceEntity("New Service", new BigDecimal("150.00"), "New Proposition"))
                .thenReturn(newService);

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.CreatService(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("New Service", response.getBody().get("designation"));
        assertEquals("New Proposition", response.getBody().get("proposition"));
        assertEquals(new BigDecimal("150.00"), response.getBody().get("prix"));
    }

    @Test
    public void testUpdateService_Success() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("designation", "Updated Service");
        request.put("prix", new BigDecimal("200.00"));
        request.put("proposition", "Updated Proposition");

        Service updatedService = new Service();
        updatedService.setId(1L);
        updatedService.setDesignation("Updated Service");
        updatedService.setPrix(new BigDecimal("200.00"));
        updatedService.setProposition("Updated Proposition");

        when(serviceF.UpdateService(1L, "Updated Service", new BigDecimal("200.00"), "Updated Proposition"))
                .thenReturn(updatedService);

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.UpdateService(1L, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Updated Service", response.getBody().get("designation"));
    }

    @Test
    public void testDeleteService_Success() {
        // Arrange
        when(serviceF.getServiceRepository()).thenReturn(serviceRepository);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.deleteService(1L);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Le Service a été supprimé avec succès.", response.getBody().get("message"));
        verify(serviceRepository, times(1)).delete(mockService);
    }

    @Test
    public void testDeleteService_NotFound() {
        // Arrange
        when(serviceF.getServiceRepository()).thenReturn(serviceRepository);
        when(serviceRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            serviceController.deleteService(999L);
        });
    }

    @Test
    public void testCreateService_WithNumberPrice() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("designation", "Number Price Service");
        request.put("prix", 150); // Integer au lieu de BigDecimal
        request.put("proposition", "Number Price Proposition");

        Service newService = new Service();
        newService.setId(3L);
        newService.setDesignation("Number Price Service");
        newService.setPrix(new BigDecimal("150"));
        newService.setProposition("Number Price Proposition");

        when(serviceF.CreateServiceEntity(eq("Number Price Service"), argThat(bd -> bd.compareTo(new BigDecimal("150")) == 0), eq("Number Price Proposition")))
                .thenReturn(newService);

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.CreatService(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Number Price Service", response.getBody().get("designation"));
        assertEquals("Number Price Proposition", response.getBody().get("proposition"));
        assertEquals(new BigDecimal("150"), response.getBody().get("prix"));
    }

    @Test
    public void testCreateService_WithDoublePrice() {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("designation", "Double Price Service");
        request.put("prix", 150.50); // Double au lieu de BigDecimal
        request.put("proposition", "Double Price Proposition");

        Service newService = new Service();
        newService.setId(4L);
        newService.setDesignation("Double Price Service");
        newService.setPrix(new BigDecimal("150.50"));
        newService.setProposition("Double Price Proposition");

        when(serviceF.CreateServiceEntity(eq("Double Price Service"), argThat(bd -> bd.compareTo(new BigDecimal("150.50")) == 0), eq("Double Price Proposition")))
                .thenReturn(newService);

        // Act
        ResponseEntity<Map<String, Object>> response = serviceController.CreatService(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().get("success"));
        assertEquals("Double Price Service", response.getBody().get("designation"));
        assertEquals("Double Price Proposition", response.getBody().get("proposition"));
        assertEquals(new BigDecimal("150.50"), response.getBody().get("prix"));
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
    public static class ServiceControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void testGetAllServices_Integration() throws Exception {
            mockMvc.perform(get("/api/services"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testCreateService_Integration() throws Exception {
            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("designation", "Integration Test Service");
            serviceData.put("prix", 250.00);
            serviceData.put("proposition", "Integration Test Proposition");

            mockMvc.perform(post("/api/services")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(serviceData)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.designation").value("Integration Test Service"));
        }
    }
} 