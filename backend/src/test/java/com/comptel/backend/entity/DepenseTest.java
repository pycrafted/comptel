package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepenseTest {

    private Depense depense;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        depense = new Depense();
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
    }

    @Test
    void testDefaultValues() {
        assertEquals(BigDecimal.ZERO, depense.getMontant());
        assertEquals(1, depense.getQuantite());
        assertNotNull(depense.getDateDepense());
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        depense.setId(id);
        assertEquals(id, depense.getId());
    }

    @Test
    void testTypeGetterAndSetter() {
        Exit.TypeDepense type = Exit.TypeDepense.ELECTRICITE;
        depense.setType(type);
        assertEquals(type, depense.getType());
    }

    @Test
    void testIntituleGetterAndSetter() {
        String intitule = "Test Dépense";
        depense.setIntitule(intitule);
        assertEquals(intitule, depense.getIntitule());
    }

    @Test
    void testMontantGetterAndSetter() {
        BigDecimal montant = new BigDecimal("150.50");
        depense.setMontant(montant);
        assertEquals(montant, depense.getMontant());
    }

    @Test
    void testQuantiteGetterAndSetter() {
        Integer quantite = 5;
        depense.setQuantite(quantite);
        assertEquals(quantite, depense.getQuantite());
    }

    @Test
    void testDateDepenseGetterAndSetter() {
        depense.setDateDepense(testDate);
        assertEquals(testDate, depense.getDateDepense());
    }

    @Test
    void testToString() {
        depense.setIntitule("Électricité");
        depense.setMontant(new BigDecimal("100.00"));
        depense.setQuantite(2);
        
        String expected = "Électricité - 100.00 x 2F.FCFA";
        assertEquals(expected, depense.toString());
    }

    @Test
    void testToStringWithNullValues() {
        depense.setIntitule(null);
        depense.setMontant(null);
        depense.setQuantite(null);
        
        String result = depense.toString();
        assertTrue(result.contains("null"));
    }

    @Test
    void testMontantWithDifferentPrecisions() {
        BigDecimal montant1 = new BigDecimal("100.123");
        BigDecimal montant2 = new BigDecimal("100.123");
        
        depense.setMontant(montant1);
        assertEquals(montant2, depense.getMontant());
    }

    @Test
    void testQuantiteWithZero() {
        depense.setQuantite(0);
        assertEquals(0, depense.getQuantite());
    }

    @Test
    void testQuantiteWithNegativeValue() {
        depense.setQuantite(-5);
        assertEquals(-5, depense.getQuantite());
    }

    @Test
    void testTypeDepenseAllValues() {
        for (Exit.TypeDepense type : Exit.TypeDepense.values()) {
            depense.setType(type);
            assertEquals(type, depense.getType());
        }
    }

    @Test
    void testDateDepenseWithNull() {
        depense.setDateDepense(null);
        assertNull(depense.getDateDepense());
    }

    @Test
    void testMontantWithNull() {
        depense.setMontant(null);
        assertNull(depense.getMontant());
    }

    @Test
    void testIntituleWithEmptyString() {
        depense.setIntitule("");
        assertEquals("", depense.getIntitule());
    }

    @Test
    void testIntituleWithSpecialCharacters() {
        String intitule = "Dépense spéciale avec émojis 🎉";
        depense.setIntitule(intitule);
        assertEquals(intitule, depense.getIntitule());
    }
} 