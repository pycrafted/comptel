package com.comptel.backend.services;

import com.comptel.backend.entity.Input;
import com.comptel.backend.entity.User;
import com.comptel.backend.repository.InputRepository;
import com.comptel.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
public class InputServiceTest {

    @InjectMocks
    private InputService inputService;

    @Mock
    private InputRepository inputRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Input mockInput;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("testuser");

        // Setup mock input
        when(mockInput.getId()).thenReturn(1L);
        when(mockInput.getTitres()).thenReturn("Test Input");
        when(mockInput.getMontants()).thenReturn(new BigDecimal("500.00"));
        when(mockInput.getModePaiement()).thenReturn(Input.ModePaiement.cash);
        when(mockInput.getCreatedAts()).thenReturn(LocalDateTime.now());
        when(mockInput.getSaveBy()).thenReturn(mockUser);
    }

    @Test
    public void testCreateInput_Success() {
        // Arrange
        String titres = "New Input";
        BigDecimal montants = new BigDecimal("750.00");
        String modePaiement = "cash";
        Long userId = 1L;

        Input newInput = new Input();
        newInput.setTitres(titres);
        newInput.setMontants(montants);
        newInput.setModePaiement(Input.ModePaiement.cash);
        newInput.setSaveBy(mockUser);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(inputRepository.save(any(Input.class))).thenReturn(newInput);

        // Act
        Input result = inputService.createInput(titres, montants, "cash", userId);

        // Assert
        assertNotNull(result);
        assertEquals(titres, result.getTitres());
        assertEquals(montants, result.getMontants());
        assertEquals(Input.ModePaiement.cash, result.getModePaiement());
        assertEquals(mockUser, result.getSaveBy());
        verify(inputRepository, times(1)).save(any(Input.class));
    }

    @Test
    public void testCreateInput_WithInvalidAmount() {
        // Arrange
        String titres = "New Input";
        BigDecimal montants = new BigDecimal("-100.00"); // Invalid negative amount
        String modePaiement = "cash";
        Long userId = 1L;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            inputService.createInput(titres, montants, modePaiement, userId);
        });
    }

    @Test
    public void testUpdateInput_Success() {
        // Arrange
        Long id = 1L;
        String titres = "Updated Input";
        BigDecimal montants = new BigDecimal("1000.00");
        String modePaiement = "om";

        Input existingInput = new Input();
        existingInput.setId(id);
        existingInput.setTitres("Old Input");
        existingInput.setMontants(new BigDecimal("500.00"));
        existingInput.setModePaiement(Input.ModePaiement.cash);

        Input updatedInput = new Input();
        updatedInput.setId(id);
        updatedInput.setTitres(titres);
        updatedInput.setMontants(montants);
        updatedInput.setModePaiement(Input.ModePaiement.om);

        when(inputRepository.findById(id)).thenReturn(Optional.of(existingInput));
        when(inputRepository.save(any(Input.class))).thenReturn(updatedInput);

        // Act
        Input result = inputService.updateInput(id, titres, montants, "om");

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(titres, result.getTitres());
        assertEquals(montants, result.getMontants());
        assertEquals(Input.ModePaiement.om, result.getModePaiement());
        verify(inputRepository, times(1)).findById(id);
        verify(inputRepository, times(1)).save(any(Input.class));
    }

    @Test
    public void testUpdateInput_NotFound() {
        // Arrange
        Long id = 999L;
        String titres = "Updated Input";
        BigDecimal montants = new BigDecimal("1000.00");
        String modePaiement = "om";

        when(inputRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inputService.updateInput(id, titres, montants, modePaiement);
        });

        assertEquals("Input non trouvé : " + id, exception.getMessage());
        verify(inputRepository, times(1)).findById(id);
        verify(inputRepository, never()).save(any(Input.class));
    }

    @Test
    public void testDeleteInput_Success() {
        // Arrange
        Long id = 1L;
        when(inputRepository.findById(id)).thenReturn(Optional.of(mockInput));
        doNothing().when(inputRepository).delete(mockInput);

        // Act
        inputService.deleteInput(id);

        // Assert
        verify(inputRepository, times(1)).findById(id);
        verify(inputRepository, times(1)).delete(mockInput);
    }

    @Test
    public void testDeleteInput_NotFound() {
        // Arrange
        Long id = 999L;
        when(inputRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inputService.deleteInput(id);
        });

        assertEquals("Input non trouvé : " + id, exception.getMessage());
        verify(inputRepository, times(1)).findById(id);
        verify(inputRepository, never()).delete(any(Input.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        Long id = 1L;
        when(inputRepository.findById(id)).thenReturn(Optional.of(mockInput));

        // Act
        Input result = inputService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Input", result.getTitres());
        verify(inputRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        Long id = 999L;
        when(inputRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inputService.findById(id);
        });

        assertEquals("Input non trouvé : " + id, exception.getMessage());
        verify(inputRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByDateRange_Success() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputRepository.findByCreatedAtsBetween(start, end)).thenReturn(inputs);

        // Act
        List<Input> result = inputService.findByDateRange(start, end);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockInput, result.get(0));
        verify(inputRepository, times(1)).findByCreatedAtsBetween(start, end);
    }

    @Test
    public void testFindByModePaiement_Success() {
        // Arrange
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputRepository.findByModePaiement(Input.ModePaiement.cash)).thenReturn(inputs);

        // Act
        List<Input> result = inputService.findByModePaiement(Input.ModePaiement.cash);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockInput, result.get(0));
        verify(inputRepository, times(1)).findByModePaiement(Input.ModePaiement.cash);
    }

    @Test
    public void testFindByUserId_Success() {
        // Arrange
        Long userId = 1L;
        List<Input> inputs = Arrays.asList(mockInput);
        when(inputRepository.findBySaveBy_Id(userId)).thenReturn(inputs);

        // Act
        List<Input> result = inputService.findByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockInput, result.get(0));
        verify(inputRepository, times(1)).findBySaveBy_Id(userId);
    }

    @Test
    public void testGetInputRepository() {
        // Act
        InputRepository result = inputService.getInputRepository();

        // Assert
        assertNotNull(result);
        assertEquals(inputRepository, result);
    }

    @Test
    public void testCreateInput_RepositoryThrowsException_ShouldPropagate() {
        // Arrange
        String titres = "New Input";
        BigDecimal montants = new BigDecimal("750.00");
        String modePaiement = "cash";
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(inputRepository.save(any(Input.class))).thenThrow(new RuntimeException("Erreur DB"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inputService.createInput(titres, montants, modePaiement, userId);
        });
    }
} 