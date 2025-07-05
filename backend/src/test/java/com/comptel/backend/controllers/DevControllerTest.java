package com.comptel.backend.controllers;

import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Tests unitaires avec Mockito
public class DevControllerTest {

    @InjectMocks
    private DevController devController;

    @Mock
    private ServiceRepository serviceRepository;

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
    public void testPopulateTestData_Success() {
        // Arrange
        when(serviceRepository.save(any(Service.class))).thenReturn(mockService);

        // Act
        ResponseEntity<String> response = devController.populateTestData();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Données de test insérées (services)", response.getBody());
        verify(serviceRepository, times(2)).save(any(Service.class)); // 2 services créés
    }

    @Test
    public void testPopulateTestData_CreatesCorrectServices() {
        // Arrange
        when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> {
            Service service = invocation.getArgument(0);
            service.setId(1L);
            return service;
        });

        // Act
        ResponseEntity<String> response = devController.populateTestData();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(serviceRepository, times(2)).save(any(Service.class));
        
        // Vérifier que les services ont les bonnes données
        verify(serviceRepository).save(argThat(service -> 
            "Service Test 1".equals(service.getDesignation()) &&
            "Proposition 1".equals(service.getProposition()) &&
            new BigDecimal("100.0").equals(service.getPrix())
        ));
        
        verify(serviceRepository).save(argThat(service -> 
            "Service Test 2".equals(service.getDesignation()) &&
            "Proposition 2".equals(service.getProposition()) &&
            new BigDecimal("200.0").equals(service.getPrix())
        ));
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
    public static class DevControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testPopulateTestData_Integration() throws Exception {
            mockMvc.perform(post("/api/dev/populate"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Données de test insérées (services)"));
        }
    }
} 