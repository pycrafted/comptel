package com.comptel.backend.services;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.entity.Depense;
import com.comptel.backend.entity.User;
import com.comptel.backend.repository.ExitRepository;
import com.comptel.backend.repository.DepenseRepository;
import com.comptel.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExitServiceTest {

    @InjectMocks
    private ExitService exitService;

    @Mock
    private ExitRepository exitRepository;

    @Mock
    private DepenseRepository depenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Exit mockExit;

    @Mock
    private User mockUser;

    @Mock
    private Depense mockDepense;

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

        // Setup mock depense
        when(mockDepense.getId()).thenReturn(1L);
        when(mockDepense.getIntitule()).thenReturn("Test Depense");
        when(mockDepense.getMontant()).thenReturn(new BigDecimal("100.00"));
        when(mockDepense.getType()).thenReturn(Exit.TypeDepense.RETRAIT);
    }

    @Test
    public void testCreateExit_Success() {
        // Arrange
        String titre = "Test Exit";
        BigDecimal montant = new BigDecimal("150.00");
        Exit.TypeDepense typeDepense = Exit.TypeDepense.RETRAIT;
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(exitRepository.save(any(Exit.class))).thenReturn(mockExit);
        when(depenseRepository.save(any(Depense.class))).thenReturn(mockDepense);

        // Act
        Exit result = exitService.createExit(titre, montant, typeDepense, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(exitRepository, times(1)).save(any(Exit.class));
        verify(depenseRepository, times(1)).save(any(Depense.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testCreateExit_UserNotFound() {
        // Arrange
        String titre = "Test Exit";
        BigDecimal montant = new BigDecimal("150.00");
        Exit.TypeDepense typeDepense = Exit.TypeDepense.RETRAIT;
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(exitRepository.save(any(Exit.class))).thenReturn(mockExit);
        when(depenseRepository.save(any(Depense.class))).thenReturn(mockDepense);

        // Act
        Exit result = exitService.createExit(titre, montant, typeDepense, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(exitRepository, times(1)).save(any(Exit.class));
        verify(depenseRepository, times(1)).save(any(Depense.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testCreateExit_WithoutTypeDepense() {
        // Arrange
        String titre = "Test Exit";
        BigDecimal montant = new BigDecimal("150.00");
        Exit.TypeDepense typeDepense = null;
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(exitRepository.save(any(Exit.class))).thenReturn(mockExit);

        // Act
        Exit result = exitService.createExit(titre, montant, typeDepense, userId);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(exitRepository, times(1)).save(any(Exit.class));
        verify(depenseRepository, never()).save(any(Depense.class));
    }

    @Test
    public void testUpdateExit_Success() {
        // Arrange
        Long id = 1L;
        String titre = "Updated Exit";
        BigDecimal montant = new BigDecimal("200.00");
        Exit.TypeDepense typeDepense = Exit.TypeDepense.REPARATION;

        when(exitRepository.findById(id)).thenReturn(Optional.of(mockExit));
        when(exitRepository.save(any(Exit.class))).thenReturn(mockExit);

        // Act
        Exit result = exitService.updateExit(id, titre, montant, typeDepense);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(exitRepository, times(1)).findById(id);
        verify(exitRepository, times(1)).save(any(Exit.class));
    }

    @Test
    public void testUpdateExit_NotFound() {
        // Arrange
        Long id = 999L;
        String titre = "Updated Exit";
        BigDecimal montant = new BigDecimal("200.00");
        Exit.TypeDepense typeDepense = Exit.TypeDepense.REPARATION;

        when(exitRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            exitService.updateExit(id, titre, montant, typeDepense);
        });
        verify(exitRepository, times(1)).findById(id);
        verify(exitRepository, never()).save(any(Exit.class));
    }

    @Test
    public void testDeleteExit_Success() {
        // Arrange
        Long id = 1L;
        when(exitRepository.findById(id)).thenReturn(Optional.of(mockExit));
        doNothing().when(exitRepository).delete(mockExit);

        // Act
        exitService.deleteExit(id);

        // Assert
        verify(exitRepository, times(1)).findById(id);
        verify(exitRepository, times(1)).delete(mockExit);
    }

    @Test
    public void testDeleteExit_NotFound() {
        // Arrange
        Long id = 999L;
        when(exitRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            exitService.deleteExit(id);
        });
        verify(exitRepository, times(1)).findById(id);
        verify(exitRepository, never()).delete(any(Exit.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        Long id = 1L;
        when(exitRepository.findById(id)).thenReturn(Optional.of(mockExit));

        // Act
        Exit result = exitService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(exitRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        Long id = 999L;
        when(exitRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            exitService.findById(id);
        });
        verify(exitRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Exit> exits = Arrays.asList(mockExit);
        when(exitRepository.findByCreatedAtBetween(start, end)).thenReturn(exits);

        // Act
        List<Exit> result = exitService.findByDateRange(start, end);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(exitRepository, times(1)).findByCreatedAtBetween(start, end);
    }

    @Test
    public void testFindByType_Success() {
        // Arrange
        Exit.TypeDepense typeDepense = Exit.TypeDepense.RETRAIT;
        List<Exit> exits = Arrays.asList(mockExit);
        when(exitRepository.findByTypeDepense(typeDepense)).thenReturn(exits);

        // Act
        List<Exit> result = exitService.findByType(typeDepense);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(exitRepository, times(1)).findByTypeDepense(typeDepense);
    }

    @Test
    public void testGetExitRepository() {
        // Act
        ExitRepository result = exitService.getExitRepository();

        // Assert
        assertNotNull(result);
        assertEquals(exitRepository, result);
    }
} 