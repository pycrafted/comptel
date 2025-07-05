package com.comptel.backend.services;

import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ServiceFTest {

    @InjectMocks
    private ServiceF serviceF;

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
    public void testCreateServiceEntity_Success() {
        // Arrange
        String designation = "New Service";
        BigDecimal prix = new BigDecimal("150.00");
        String proposition = "New Proposition";

        Service newService = new Service();
        newService.setDesignation(designation);
        newService.setPrix(prix);
        newService.setProposition(proposition);

        when(serviceRepository.save(any(Service.class))).thenReturn(newService);

        // Act
        Service result = serviceF.CreateServiceEntity(designation, prix, proposition);

        // Assert
        assertNotNull(result);
        assertEquals(designation, result.getDesignation());
        assertEquals(prix, result.getPrix());
        assertEquals(proposition, result.getProposition());
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    public void testUpdateService_Success() {
        // Arrange
        Long id = 1L;
        String designation = "Updated Service";
        BigDecimal prix = new BigDecimal("200.00");
        String proposition = "Updated Proposition";

        Service existingService = new Service();
        existingService.setId(id);
        existingService.setDesignation("Old Service");
        existingService.setPrix(new BigDecimal("100.00"));
        existingService.setProposition("Old Proposition");

        Service updatedService = new Service();
        updatedService.setId(id);
        updatedService.setDesignation(designation);
        updatedService.setPrix(prix);
        updatedService.setProposition(proposition);

        when(serviceRepository.findById(id)).thenReturn(Optional.of(existingService));
        when(serviceRepository.save(any(Service.class))).thenReturn(updatedService);

        // Act
        Service result = serviceF.UpdateService(id, designation, prix, proposition);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(designation, result.getDesignation());
        assertEquals(prix, result.getPrix());
        assertEquals(proposition, result.getProposition());
        verify(serviceRepository, times(1)).findById(id);
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    public void testUpdateService_NotFound() {
        // Arrange
        Long id = 999L;
        String designation = "Updated Service";
        BigDecimal prix = new BigDecimal("200.00");
        String proposition = "Updated Proposition";

        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceF.UpdateService(id, designation, prix, proposition);
        });

        assertEquals("Service non trouvé : " + id, exception.getMessage());
        verify(serviceRepository, times(1)).findById(id);
        verify(serviceRepository, never()).save(any(Service.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        Long id = 1L;
        when(serviceRepository.findById(id)).thenReturn(Optional.of(mockService));

        // Act
        Service result = serviceF.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Service", result.getDesignation());
        verify(serviceRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        Long id = 999L;
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            serviceF.findById(id);
        });

        assertEquals("Service non trouvé : " + id, exception.getMessage());
        verify(serviceRepository, times(1)).findById(id);
    }

    @Test
    public void testGetServiceRepository() {
        // Act
        ServiceRepository result = serviceF.getServiceRepository();

        // Assert
        assertNotNull(result);
        assertEquals(serviceRepository, result);
    }
} 