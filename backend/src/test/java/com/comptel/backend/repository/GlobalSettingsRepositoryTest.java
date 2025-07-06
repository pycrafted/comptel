package com.comptel.backend.repository;

import com.comptel.backend.entity.GlobalSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class GlobalSettingsRepositoryTest {

    @Autowired
    private GlobalSettingsRepository globalSettingsRepository;

    private GlobalSettings testSettings1;
    private GlobalSettings testSettings2;
    private GlobalSettings testSettings3;

    @BeforeEach
    void setUp() {
        globalSettingsRepository.deleteAll();

        testSettings1 = new GlobalSettings();
        testSettings1.setUseDeliveryConfirmation(true);
        testSettings1.setUsePartialPayment(true);
        testSettings1.setUseAntidate(true);

        testSettings2 = new GlobalSettings();
        testSettings2.setUseDeliveryConfirmation(false);
        testSettings2.setUsePartialPayment(true);
        testSettings2.setUseAntidate(false);

        testSettings3 = new GlobalSettings();
        testSettings3.setUseDeliveryConfirmation(true);
        testSettings3.setUsePartialPayment(false);
        testSettings3.setUseAntidate(true);
    }

    @Test
    void testSaveGlobalSettings() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        
        assertNotNull(savedSettings.getId());
        assertTrue(savedSettings.isUseDeliveryConfirmation());
        assertTrue(savedSettings.isUsePartialPayment());
        assertTrue(savedSettings.isUseAntidate());
    }

    @Test
    void testFindById() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        Optional<GlobalSettings> foundSettings = globalSettingsRepository.findById(savedSettings.getId());
        
        assertTrue(foundSettings.isPresent());
        assertEquals(savedSettings.getId(), foundSettings.get().getId());
        assertTrue(foundSettings.get().isUseDeliveryConfirmation());
        assertTrue(foundSettings.get().isUsePartialPayment());
        assertTrue(foundSettings.get().isUseAntidate());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<GlobalSettings> foundSettings = globalSettingsRepository.findById(999L);
        assertFalse(foundSettings.isPresent());
    }

    @Test
    void testFindAll() {
        globalSettingsRepository.save(testSettings1);
        globalSettingsRepository.save(testSettings2);
        globalSettingsRepository.save(testSettings3);
        
        List<GlobalSettings> allSettings = globalSettingsRepository.findAll();
        assertEquals(3, allSettings.size());
    }

    @Test
    void testUpdateGlobalSettings() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        savedSettings.setUseDeliveryConfirmation(false);
        savedSettings.setUsePartialPayment(false);
        savedSettings.setUseAntidate(false);
        
        GlobalSettings updatedSettings = globalSettingsRepository.save(savedSettings);
        assertFalse(updatedSettings.isUseDeliveryConfirmation());
        assertFalse(updatedSettings.isUsePartialPayment());
        assertFalse(updatedSettings.isUseAntidate());
    }

    @Test
    void testDeleteGlobalSettings() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        Long settingsId = savedSettings.getId();
        
        globalSettingsRepository.deleteById(settingsId);
        
        Optional<GlobalSettings> foundSettings = globalSettingsRepository.findById(settingsId);
        assertFalse(foundSettings.isPresent());
    }

    @Test
    void testCount() {
        globalSettingsRepository.save(testSettings1);
        globalSettingsRepository.save(testSettings2);
        
        long count = globalSettingsRepository.count();
        assertEquals(2, count);
    }

    @Test
    void testExistsById() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        
        assertTrue(globalSettingsRepository.existsById(savedSettings.getId()));
        assertFalse(globalSettingsRepository.existsById(999L));
    }

    @Test
    void testSaveAll() {
        List<GlobalSettings> settings = List.of(testSettings1, testSettings2, testSettings3);
        List<GlobalSettings> savedSettings = globalSettingsRepository.saveAll(settings);
        
        assertEquals(3, savedSettings.size());
        assertTrue(savedSettings.stream().allMatch(s -> s.getId() != null));
    }

    @Test
    void testDeleteAll() {
        globalSettingsRepository.save(testSettings1);
        globalSettingsRepository.save(testSettings2);
        
        globalSettingsRepository.deleteAll();
        
        assertEquals(0, globalSettingsRepository.count());
    }

    @Test
    void testDeleteAllById() {
        GlobalSettings saved1 = globalSettingsRepository.save(testSettings1);
        GlobalSettings saved2 = globalSettingsRepository.save(testSettings2);
        GlobalSettings saved3 = globalSettingsRepository.save(testSettings3);
        
        globalSettingsRepository.deleteAllById(List.of(saved1.getId(), saved2.getId()));
        
        assertEquals(1, globalSettingsRepository.count());
        assertTrue(globalSettingsRepository.existsById(saved3.getId()));
    }

    @Test
    void testDefaultValues() {
        GlobalSettings settings = new GlobalSettings();
        GlobalSettings savedSettings = globalSettingsRepository.save(settings);
        
        assertTrue(savedSettings.isUseDeliveryConfirmation());
        assertTrue(savedSettings.isUsePartialPayment());
        assertTrue(savedSettings.isUseAntidate());
    }

    @Test
    void testAllFalseSettings() {
        GlobalSettings allFalseSettings = new GlobalSettings();
        allFalseSettings.setUseDeliveryConfirmation(false);
        allFalseSettings.setUsePartialPayment(false);
        allFalseSettings.setUseAntidate(false);
        
        GlobalSettings savedSettings = globalSettingsRepository.save(allFalseSettings);
        
        assertFalse(savedSettings.isUseDeliveryConfirmation());
        assertFalse(savedSettings.isUsePartialPayment());
        assertFalse(savedSettings.isUseAntidate());
    }

    @Test
    void testMixedSettings() {
        GlobalSettings mixedSettings = new GlobalSettings();
        mixedSettings.setUseDeliveryConfirmation(true);
        mixedSettings.setUsePartialPayment(false);
        mixedSettings.setUseAntidate(true);
        
        GlobalSettings savedSettings = globalSettingsRepository.save(mixedSettings);
        
        assertTrue(savedSettings.isUseDeliveryConfirmation());
        assertFalse(savedSettings.isUsePartialPayment());
        assertTrue(savedSettings.isUseAntidate());
    }

    @Test
    void testMultipleUpdates() {
        GlobalSettings savedSettings = globalSettingsRepository.save(testSettings1);
        
        // First update
        savedSettings.setUseDeliveryConfirmation(false);
        GlobalSettings updated1 = globalSettingsRepository.save(savedSettings);
        assertFalse(updated1.isUseDeliveryConfirmation());
        
        // Second update
        savedSettings.setUsePartialPayment(false);
        GlobalSettings updated2 = globalSettingsRepository.save(savedSettings);
        assertFalse(updated2.isUseDeliveryConfirmation());
        assertFalse(updated2.isUsePartialPayment());
        
        // Third update
        savedSettings.setUseAntidate(false);
        GlobalSettings updated3 = globalSettingsRepository.save(savedSettings);
        assertFalse(updated3.isUseDeliveryConfirmation());
        assertFalse(updated3.isUsePartialPayment());
        assertFalse(updated3.isUseAntidate());
    }


} 