package com.comptel.backend.repository;

import com.comptel.backend.entity.Depense;
import com.comptel.backend.entity.Exit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DepenseRepositoryTest {

    @Autowired
    private DepenseRepository depenseRepository;

    private Depense testDepense1;
    private Depense testDepense2;
    private Depense testDepense3;

    @BeforeEach
    void setUp() {
        depenseRepository.deleteAll();

        testDepense1 = new Depense();
        testDepense1.setIntitule("Électricité");
        testDepense1.setMontant(new BigDecimal("100.00"));
        testDepense1.setQuantite(1);
        testDepense1.setType(Exit.TypeDepense.ELECTRICITE);
        testDepense1.setDateDepense(LocalDateTime.of(2024, 1, 15, 10, 0));

        testDepense2 = new Depense();
        testDepense2.setIntitule("Produit lavage");
        testDepense2.setMontant(new BigDecimal("50.00"));
        testDepense2.setQuantite(2);
        testDepense2.setType(Exit.TypeDepense.PRODUIT_LAVAGE);
        testDepense2.setDateDepense(LocalDateTime.of(2024, 1, 16, 14, 30));

        testDepense3 = new Depense();
        testDepense3.setIntitule("Salaire");
        testDepense3.setMontant(new BigDecimal("500.00"));
        testDepense3.setQuantite(1);
        testDepense3.setType(Exit.TypeDepense.SALAIRE);
        testDepense3.setDateDepense(LocalDateTime.of(2024, 1, 17, 9, 0));
    }

    @Test
    void testSaveDepense() {
        Depense savedDepense = depenseRepository.save(testDepense1);
        
        assertNotNull(savedDepense.getId());
        assertEquals("Électricité", savedDepense.getIntitule());
        assertEquals(new BigDecimal("100.00"), savedDepense.getMontant());
        assertEquals(1, savedDepense.getQuantite());
        assertEquals(Exit.TypeDepense.ELECTRICITE, savedDepense.getType());
    }

    @Test
    void testFindById() {
        Depense savedDepense = depenseRepository.save(testDepense1);
        Optional<Depense> foundDepense = depenseRepository.findById(savedDepense.getId());
        
        assertTrue(foundDepense.isPresent());
        assertEquals(savedDepense.getId(), foundDepense.get().getId());
        assertEquals("Électricité", foundDepense.get().getIntitule());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Depense> foundDepense = depenseRepository.findById(999L);
        assertFalse(foundDepense.isPresent());
    }

    @Test
    void testFindAll() {
        depenseRepository.save(testDepense1);
        depenseRepository.save(testDepense2);
        depenseRepository.save(testDepense3);
        
        List<Depense> allDepenses = depenseRepository.findAll();
        assertEquals(3, allDepenses.size());
    }

    @Test
    void testUpdateDepense() {
        Depense savedDepense = depenseRepository.save(testDepense1);
        savedDepense.setMontant(new BigDecimal("150.00"));
        savedDepense.setQuantite(3);
        
        Depense updatedDepense = depenseRepository.save(savedDepense);
        assertEquals(new BigDecimal("150.00"), updatedDepense.getMontant());
        assertEquals(3, updatedDepense.getQuantite());
    }

    @Test
    void testDeleteDepense() {
        Depense savedDepense = depenseRepository.save(testDepense1);
        Long depenseId = savedDepense.getId();
        
        depenseRepository.deleteById(depenseId);
        
        Optional<Depense> foundDepense = depenseRepository.findById(depenseId);
        assertFalse(foundDepense.isPresent());
    }

    @Test
    void testFindByDateDepenseBetween() {
        depenseRepository.save(testDepense1); // 2024-01-15
        depenseRepository.save(testDepense2); // 2024-01-16
        depenseRepository.save(testDepense3); // 2024-01-17
        
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 23, 59);
        
        List<Depense> depenses = depenseRepository.findByDateDepenseBetween(start, end);
        assertEquals(2, depenses.size());
        
        // Verify the depenses are within the date range
        for (Depense depense : depenses) {
            assertTrue(depense.getDateDepense().isAfter(start) || depense.getDateDepense().isEqual(start));
            assertTrue(depense.getDateDepense().isBefore(end) || depense.getDateDepense().isEqual(end));
        }
    }

    @Test
    void testFindByDateDepenseBetweenNoResults() {
        depenseRepository.save(testDepense1);
        
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 2, 28, 23, 59);
        
        List<Depense> depenses = depenseRepository.findByDateDepenseBetween(start, end);
        assertEquals(0, depenses.size());
    }

    @Test
    void testFindByType() {
        depenseRepository.save(testDepense1); // ELECTRICITE
        depenseRepository.save(testDepense2); // PRODUIT_LAVAGE
        depenseRepository.save(testDepense3); // SALAIRE
        
        List<Depense> electriciteDepenses = depenseRepository.findByType(Exit.TypeDepense.ELECTRICITE);
        assertEquals(1, electriciteDepenses.size());
        assertEquals(Exit.TypeDepense.ELECTRICITE, electriciteDepenses.get(0).getType());
        
        List<Depense> salaireDepenses = depenseRepository.findByType(Exit.TypeDepense.SALAIRE);
        assertEquals(1, salaireDepenses.size());
        assertEquals(Exit.TypeDepense.SALAIRE, salaireDepenses.get(0).getType());
    }

    @Test
    void testFindByTypeNoResults() {
        depenseRepository.save(testDepense1); // ELECTRICITE
        
        List<Depense> salaireDepenses = depenseRepository.findByType(Exit.TypeDepense.SALAIRE);
        assertEquals(0, salaireDepenses.size());
    }

    @Test
    void testCount() {
        depenseRepository.save(testDepense1);
        depenseRepository.save(testDepense2);
        
        long count = depenseRepository.count();
        assertEquals(2, count);
    }

    @Test
    void testExistsById() {
        Depense savedDepense = depenseRepository.save(testDepense1);
        
        assertTrue(depenseRepository.existsById(savedDepense.getId()));
        assertFalse(depenseRepository.existsById(999L));
    }

    @Test
    void testSaveAll() {
        List<Depense> depenses = List.of(testDepense1, testDepense2, testDepense3);
        List<Depense> savedDepenses = depenseRepository.saveAll(depenses);
        
        assertEquals(3, savedDepenses.size());
        assertTrue(savedDepenses.stream().allMatch(d -> d.getId() != null));
    }

    @Test
    void testDeleteAll() {
        depenseRepository.save(testDepense1);
        depenseRepository.save(testDepense2);
        
        depenseRepository.deleteAll();
        
        assertEquals(0, depenseRepository.count());
    }

    @Test
    void testDeleteAllById() {
        Depense saved1 = depenseRepository.save(testDepense1);
        Depense saved2 = depenseRepository.save(testDepense2);
        Depense saved3 = depenseRepository.save(testDepense3);
        
        depenseRepository.deleteAllById(List.of(saved1.getId(), saved2.getId()));
        
        assertEquals(1, depenseRepository.count());
        assertTrue(depenseRepository.existsById(saved3.getId()));
    }
} 