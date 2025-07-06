package com.comptel.backend.repository;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.entity.User;
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
class ExitRepositoryTest {

    @Autowired
    private ExitRepository exitRepository;

    @Autowired
    private com.comptel.backend.repository.UserRepository userRepository;

    private Exit testExit1;
    private Exit testExit2;
    private Exit testExit3;
    private User testUser;

    @BeforeEach
    void setUp() {
        exitRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(false);
        testUser = userRepository.save(testUser);

        testExit1 = new Exit();
        testExit1.setTitre("Retrait d'argent");
        testExit1.setMontant(new BigDecimal("100.00"));
        testExit1.setTypeDepense(Exit.TypeDepense.RETRAIT);
        testExit1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        testExit1.setSaveBy(testUser);

        testExit2 = new Exit();
        testExit2.setTitre("Réparation machine");
        testExit2.setMontant(new BigDecimal("250.00"));
        testExit2.setTypeDepense(Exit.TypeDepense.REPARATION);
        testExit2.setCreatedAt(LocalDateTime.of(2024, 1, 16, 14, 30));
        testExit2.setSaveBy(testUser);

        testExit3 = new Exit();
        testExit3.setTitre("Paiement salaire");
        testExit3.setMontant(new BigDecimal("500.00"));
        testExit3.setTypeDepense(Exit.TypeDepense.SALAIRE);
        testExit3.setCreatedAt(LocalDateTime.of(2024, 1, 17, 9, 0));
        testExit3.setSaveBy(testUser);
    }

    @Test
    void testSaveExit() {
        Exit savedExit = exitRepository.save(testExit1);
        
        assertNotNull(savedExit.getId());
        assertEquals("Retrait d'argent", savedExit.getTitre());
        assertEquals(new BigDecimal("100.00"), savedExit.getMontant());
        assertEquals(Exit.TypeDepense.RETRAIT, savedExit.getTypeDepense());
        assertEquals(testUser, savedExit.getSaveBy());
    }

    @Test
    void testFindById() {
        Exit savedExit = exitRepository.save(testExit1);
        Optional<Exit> foundExit = exitRepository.findById(savedExit.getId());
        
        assertTrue(foundExit.isPresent());
        assertEquals(savedExit.getId(), foundExit.get().getId());
        assertEquals("Retrait d'argent", foundExit.get().getTitre());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Exit> foundExit = exitRepository.findById(999L);
        assertFalse(foundExit.isPresent());
    }

    @Test
    void testFindAll() {
        exitRepository.save(testExit1);
        exitRepository.save(testExit2);
        exitRepository.save(testExit3);
        
        List<Exit> allExits = exitRepository.findAll();
        assertEquals(3, allExits.size());
    }

    @Test
    void testUpdateExit() {
        Exit savedExit = exitRepository.save(testExit1);
        savedExit.setMontant(new BigDecimal("150.00"));
        savedExit.setTitre("Retrait modifié");
        
        Exit updatedExit = exitRepository.save(savedExit);
        assertEquals(new BigDecimal("150.00"), updatedExit.getMontant());
        assertEquals("Retrait modifié", updatedExit.getTitre());
    }

    @Test
    void testDeleteExit() {
        Exit savedExit = exitRepository.save(testExit1);
        Long exitId = savedExit.getId();
        
        exitRepository.deleteById(exitId);
        
        Optional<Exit> foundExit = exitRepository.findById(exitId);
        assertFalse(foundExit.isPresent());
    }

    @Test
    void testFindByCreatedAtBetween() {
        exitRepository.save(testExit1); // 2024-01-15
        exitRepository.save(testExit2); // 2024-01-16
        exitRepository.save(testExit3); // 2024-01-17
        
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 16, 23, 59);
        
        List<Exit> exits = exitRepository.findByCreatedAtBetween(start, end);
        assertEquals(2, exits.size());
        
        // Verify the exits are within the date range
        for (Exit exit : exits) {
            assertTrue(exit.getCreatedAt().isAfter(start) || exit.getCreatedAt().isEqual(start));
            assertTrue(exit.getCreatedAt().isBefore(end) || exit.getCreatedAt().isEqual(end));
        }
    }

    @Test
    void testFindByCreatedAtBetweenNoResults() {
        exitRepository.save(testExit1);
        
        LocalDateTime start = LocalDateTime.of(2024, 2, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 2, 28, 23, 59);
        
        List<Exit> exits = exitRepository.findByCreatedAtBetween(start, end);
        assertEquals(0, exits.size());
    }

    @Test
    void testFindByTypeDepense() {
        exitRepository.save(testExit1); // RETRAIT
        exitRepository.save(testExit2); // REPARATION
        exitRepository.save(testExit3); // SALAIRE
        
        List<Exit> retraitExits = exitRepository.findByTypeDepense(Exit.TypeDepense.RETRAIT);
        assertEquals(1, retraitExits.size());
        assertEquals(Exit.TypeDepense.RETRAIT, retraitExits.get(0).getTypeDepense());
        
        List<Exit> salaireExits = exitRepository.findByTypeDepense(Exit.TypeDepense.SALAIRE);
        assertEquals(1, salaireExits.size());
        assertEquals(Exit.TypeDepense.SALAIRE, salaireExits.get(0).getTypeDepense());
    }

    @Test
    void testFindByTypeDepenseNoResults() {
        exitRepository.save(testExit1); // RETRAIT
        
        List<Exit> salaireExits = exitRepository.findByTypeDepense(Exit.TypeDepense.SALAIRE);
        assertEquals(0, salaireExits.size());
    }

    @Test
    void testCount() {
        exitRepository.save(testExit1);
        exitRepository.save(testExit2);
        
        long count = exitRepository.count();
        assertEquals(2, count);
    }

    @Test
    void testExistsById() {
        Exit savedExit = exitRepository.save(testExit1);
        
        assertTrue(exitRepository.existsById(savedExit.getId()));
        assertFalse(exitRepository.existsById(999L));
    }

    @Test
    void testSaveAll() {
        List<Exit> exits = List.of(testExit1, testExit2, testExit3);
        List<Exit> savedExits = exitRepository.saveAll(exits);
        
        assertEquals(3, savedExits.size());
        assertTrue(savedExits.stream().allMatch(e -> e.getId() != null));
    }

    @Test
    void testDeleteAll() {
        exitRepository.save(testExit1);
        exitRepository.save(testExit2);
        
        exitRepository.deleteAll();
        
        assertEquals(0, exitRepository.count());
    }

    @Test
    void testDeleteAllById() {
        Exit saved1 = exitRepository.save(testExit1);
        Exit saved2 = exitRepository.save(testExit2);
        Exit saved3 = exitRepository.save(testExit3);
        
        exitRepository.deleteAllById(List.of(saved1.getId(), saved2.getId()));
        
        assertEquals(1, exitRepository.count());
        assertTrue(exitRepository.existsById(saved3.getId()));
    }

    @Test
    void testExitWithNullValues() {
        Exit exitWithNulls = new Exit();
        exitWithNulls.setTitre(null);
        exitWithNulls.setMontant(null);
        exitWithNulls.setTypeDepense(null);
        exitWithNulls.setSaveBy(null);
        
        Exit savedExit = exitRepository.save(exitWithNulls);
        assertNotNull(savedExit.getId());
        
        Optional<Exit> foundExit = exitRepository.findById(savedExit.getId());
        assertTrue(foundExit.isPresent());
        assertNull(foundExit.get().getTitre());
        assertNull(foundExit.get().getMontant());
        assertNull(foundExit.get().getTypeDepense());
        assertNull(foundExit.get().getSaveBy());
    }

    @Test
    void testExitWithEmptyString() {
        Exit exitWithEmptyString = new Exit();
        exitWithEmptyString.setTitre("");
        exitWithEmptyString.setMontant(BigDecimal.ZERO);
        exitWithEmptyString.setTypeDepense(Exit.TypeDepense.FRAIS_DIVERS);
        exitWithEmptyString.setSaveBy(testUser);
        
        Exit savedExit = exitRepository.save(exitWithEmptyString);
        assertEquals("", savedExit.getTitre());
    }

    @Test
    void testAllTypeDepenseValues() {
        for (Exit.TypeDepense type : Exit.TypeDepense.values()) {
            Exit exit = new Exit();
            exit.setTitre("Test " + type.name());
            exit.setMontant(new BigDecimal("100.00"));
            exit.setTypeDepense(type);
            exit.setSaveBy(testUser);
            
            Exit savedExit = exitRepository.save(exit);
            assertEquals(type, savedExit.getTypeDepense());
        }
        
        assertEquals(8, exitRepository.count()); // 8 types in the enum
    }
} 