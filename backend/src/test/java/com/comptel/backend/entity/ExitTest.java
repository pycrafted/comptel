package com.comptel.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExitTest {

    private Exit exit;
    private User testUser;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        exit = new Exit();
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
    }

    @Test
    void testDefaultValues() {
        assertEquals(BigDecimal.ZERO, exit.getMontant());
        assertNotNull(exit.getCreatedAt());
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        exit.setId(id);
        assertEquals(id, exit.getId());
    }

    @Test
    void testTitreGetterAndSetter() {
        String titre = "Test Exit";
        exit.setTitre(titre);
        assertEquals(titre, exit.getTitre());
    }

    @Test
    void testMontantGetterAndSetter() {
        BigDecimal montant = new BigDecimal("150.50");
        exit.setMontant(montant);
        assertEquals(montant, exit.getMontant());
    }

    @Test
    void testCreatedAtGetterAndSetter() {
        exit.setCreatedAt(testDate);
        assertEquals(testDate, exit.getCreatedAt());
    }

    @Test
    void testSaveByGetterAndSetter() {
        exit.setSaveBy(testUser);
        assertEquals(testUser, exit.getSaveBy());
    }

    @Test
    void testTypeDepenseGetterAndSetter() {
        Exit.TypeDepense type = Exit.TypeDepense.RETRAIT;
        exit.setTypeDepense(type);
        assertEquals(type, exit.getTypeDepense());
    }

    @Test
    void testTypeDepenseEnumValues() {
        Exit.TypeDepense[] types = Exit.TypeDepense.values();
        assertEquals(8, types.length);
        
        assertArrayEquals(new Exit.TypeDepense[]{
            Exit.TypeDepense.RETRAIT,
            Exit.TypeDepense.REPARATION,
            Exit.TypeDepense.SALAIRE,
            Exit.TypeDepense.FACTURE_EAU,
            Exit.TypeDepense.ELECTRICITE,
            Exit.TypeDepense.PRODUIT_REPASSAGE,
            Exit.TypeDepense.PRODUIT_LAVAGE,
            Exit.TypeDepense.FRAIS_DIVERS
        }, types);
    }

    @Test
    void testTypeDepenseLabels() {
        assertEquals("Retrait", Exit.TypeDepense.RETRAIT.getLabel());
        assertEquals("Réparation", Exit.TypeDepense.REPARATION.getLabel());
        assertEquals("Salaire", Exit.TypeDepense.SALAIRE.getLabel());
        assertEquals("Facture eau", Exit.TypeDepense.FACTURE_EAU.getLabel());
        assertEquals("Électricité", Exit.TypeDepense.ELECTRICITE.getLabel());
        assertEquals("Produit repassage", Exit.TypeDepense.PRODUIT_REPASSAGE.getLabel());
        assertEquals("Produit lavage", Exit.TypeDepense.PRODUIT_LAVAGE.getLabel());
        assertEquals("Frais divers", Exit.TypeDepense.FRAIS_DIVERS.getLabel());
    }

    @Test
    void testMontantWithDifferentPrecisions() {
        BigDecimal montant1 = new BigDecimal("100.123");
        BigDecimal montant2 = new BigDecimal("100.123");
        
        exit.setMontant(montant1);
        assertEquals(montant2, exit.getMontant());
    }

    @Test
    void testMontantWithZero() {
        exit.setMontant(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, exit.getMontant());
    }

    @Test
    void testMontantWithNegativeValue() {
        BigDecimal negativeMontant = new BigDecimal("-50.00");
        exit.setMontant(negativeMontant);
        assertEquals(negativeMontant, exit.getMontant());
    }

    @Test
    void testTitreWithEmptyString() {
        exit.setTitre("");
        assertEquals("", exit.getTitre());
    }

    @Test
    void testTitreWithNull() {
        exit.setTitre(null);
        assertNull(exit.getTitre());
    }

    @Test
    void testTitreWithSpecialCharacters() {
        String titre = "Sortie spéciale avec émojis 🎉";
        exit.setTitre(titre);
        assertEquals(titre, exit.getTitre());
    }

    @Test
    void testSaveByWithNull() {
        exit.setSaveBy(null);
        assertNull(exit.getSaveBy());
    }

    @Test
    void testCreatedAtWithNull() {
        exit.setCreatedAt(null);
        assertNull(exit.getCreatedAt());
    }

    @Test
    void testMontantWithNull() {
        exit.setMontant(null);
        assertNull(exit.getMontant());
    }

    @Test
    void testTypeDepenseWithNull() {
        exit.setTypeDepense(null);
        assertNull(exit.getTypeDepense());
    }

    @Test
    void testAllTypeDepenseValues() {
        for (Exit.TypeDepense type : Exit.TypeDepense.values()) {
            exit.setTypeDepense(type);
            assertEquals(type, exit.getTypeDepense());
        }
    }

    @Test
    void testTypeDepenseValueOf() {
        Exit.TypeDepense type = Exit.TypeDepense.valueOf("RETRAIT");
        assertEquals(Exit.TypeDepense.RETRAIT, type);
    }

    @Test
    void testTypeDepenseOrdinal() {
        assertEquals(0, Exit.TypeDepense.RETRAIT.ordinal());
        assertEquals(1, Exit.TypeDepense.REPARATION.ordinal());
        assertEquals(2, Exit.TypeDepense.SALAIRE.ordinal());
        assertEquals(3, Exit.TypeDepense.FACTURE_EAU.ordinal());
        assertEquals(4, Exit.TypeDepense.ELECTRICITE.ordinal());
        assertEquals(5, Exit.TypeDepense.PRODUIT_REPASSAGE.ordinal());
        assertEquals(6, Exit.TypeDepense.PRODUIT_LAVAGE.ordinal());
        assertEquals(7, Exit.TypeDepense.FRAIS_DIVERS.ordinal());
    }
} 