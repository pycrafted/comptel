package com.comptel.backend.repository;

import com.comptel.backend.entity.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ServiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ServiceRepository serviceRepository;

    private Service testService;

    @BeforeEach
    public void setUp() {
        testService = new Service();
        testService.setDesignation("Test Service");
        testService.setProposition("Test Proposition");
        testService.setPrix(new BigDecimal("100.00"));
    }

    @Test
    public void testSaveService() {
        // Act
        Service savedService = serviceRepository.save(testService);

        // Assert
        assertNotNull(savedService.getId());
        assertEquals("Test Service", savedService.getDesignation());
        assertEquals("Test Proposition", savedService.getProposition());
        assertEquals(new BigDecimal("100.00"), savedService.getPrix());
    }

    @Test
    public void testFindById_ServiceExists() {
        // Arrange
        Service savedService = entityManager.persistAndFlush(testService);

        // Act
        Optional<Service> foundService = serviceRepository.findById(savedService.getId());

        // Assert
        assertTrue(foundService.isPresent());
        assertEquals(savedService.getId(), foundService.get().getId());
        assertEquals("Test Service", foundService.get().getDesignation());
    }

    @Test
    public void testFindById_ServiceNotExists() {
        // Act
        Optional<Service> foundService = serviceRepository.findById(999L);

        // Assert
        assertFalse(foundService.isPresent());
    }

    @Test
    public void testFindAll() {
        // Arrange
        Service service1 = new Service();
        service1.setDesignation("Service 1");
        service1.setProposition("Proposition 1");
        service1.setPrix(new BigDecimal("100.00"));

        Service service2 = new Service();
        service2.setDesignation("Service 2");
        service2.setProposition("Proposition 2");
        service2.setPrix(new BigDecimal("200.00"));

        entityManager.persistAndFlush(service1);
        entityManager.persistAndFlush(service2);

        // Act
        List<Service> services = serviceRepository.findAll();

        // Assert
        assertTrue(services.size() >= 2);
        assertTrue(services.stream().anyMatch(s -> "Service 1".equals(s.getDesignation())));
        assertTrue(services.stream().anyMatch(s -> "Service 2".equals(s.getDesignation())));
    }

    @Test
    public void testUpdateService() {
        // Arrange
        Service savedService = entityManager.persistAndFlush(testService);
        savedService.setDesignation("Updated Service");
        savedService.setPrix(new BigDecimal("150.00"));

        // Act
        Service updatedService = serviceRepository.save(savedService);

        // Assert
        assertEquals(savedService.getId(), updatedService.getId());
        assertEquals("Updated Service", updatedService.getDesignation());
        assertEquals(new BigDecimal("150.00"), updatedService.getPrix());
    }

    @Test
    public void testDeleteService() {
        // Arrange
        Service savedService = entityManager.persistAndFlush(testService);

        // Act
        serviceRepository.delete(savedService);

        // Assert
        Optional<Service> foundService = serviceRepository.findById(savedService.getId());
        assertFalse(foundService.isPresent());
    }

    @Test
    public void testSaveServiceWithHighPrice() {
        // Arrange
        testService.setPrix(new BigDecimal("9999.99"));

        // Act
        Service savedService = serviceRepository.save(testService);

        // Assert
        assertNotNull(savedService.getId());
        assertEquals(new BigDecimal("9999.99"), savedService.getPrix());
    }

    @Test
    public void testSaveServiceWithZeroPrice() {
        // Arrange
        testService.setPrix(BigDecimal.ZERO);

        // Act
        Service savedService = serviceRepository.save(testService);

        // Assert
        assertNotNull(savedService.getId());
        assertEquals(BigDecimal.ZERO, savedService.getPrix());
    }

    @Test
    public void testSaveServiceWithLongDesignation() {
        // Arrange
        String longDesignation = "This is a very long service designation that might exceed normal length";
        testService.setDesignation(longDesignation);

        // Act
        Service savedService = serviceRepository.save(testService);

        // Assert
        assertNotNull(savedService.getId());
        assertEquals(longDesignation, savedService.getDesignation());
    }

    @Test
    public void testSaveServiceWithSpecialCharacters() {
        // Arrange
        testService.setDesignation("Service avec caractères spéciaux: éàçù");
        testService.setProposition("Proposition avec € et £");

        // Act
        Service savedService = serviceRepository.save(testService);

        // Assert
        assertNotNull(savedService.getId());
        assertEquals("Service avec caractères spéciaux: éàçù", savedService.getDesignation());
        assertEquals("Proposition avec € et £", savedService.getProposition());
    }

    @Test
    public void testCountServices() {
        // Arrange
        Service service1 = new Service();
        service1.setDesignation("Service 1");
        service1.setProposition("Proposition 1");
        service1.setPrix(new BigDecimal("100.00"));

        Service service2 = new Service();
        service2.setDesignation("Service 2");
        service2.setProposition("Proposition 2");
        service2.setPrix(new BigDecimal("200.00"));

        entityManager.persistAndFlush(service1);
        entityManager.persistAndFlush(service2);

        // Act
        long count = serviceRepository.count();

        // Assert
        assertTrue(count >= 2);
    }

    @Test
    public void testExistsById() {
        // Arrange
        Service savedService = entityManager.persistAndFlush(testService);

        // Act
        boolean exists = serviceRepository.existsById(savedService.getId());
        boolean notExists = serviceRepository.existsById(999L);

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }
} 