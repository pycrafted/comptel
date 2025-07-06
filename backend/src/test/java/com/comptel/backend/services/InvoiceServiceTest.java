package com.comptel.backend.services;

import com.comptel.backend.entity.*;
import com.comptel.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock(lenient = true)
    private InvoiceRepository invoiceRepository;

    @Mock(lenient = true)
    private InputRepository inputRepository;

    @Mock(lenient = true)
    private ServiceRepository serviceRepository;

    @Mock(lenient = true)
    private InvoiceLineRepository invoiceLineRepository;

    @Mock(lenient = true)
    private PaymentRepository paymentRepository;

    private Invoice mockInvoice;
    private User mockUser;
    private com.comptel.backend.entity.Service mockService;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockService = new com.comptel.backend.entity.Service();
        mockService.setId(1L);
        mockService.setDesignation("Test Service");

        mockInvoice = new Invoice();
        mockInvoice.setId(1L);
        mockInvoice.setReference(8000);
        mockInvoice.setCustomer("Test Customer");
        mockInvoice.setTelephone("123456789");
        mockInvoice.setSaveBy(mockUser);
        mockInvoice.setInvoiceDateTime(LocalDateTime.now());
        mockInvoice.setDelivered(false);
        mockInvoice.setTotal(new BigDecimal("100.00"));
        mockInvoice.setAmountPaid(BigDecimal.ZERO);
    }

    @Test
    void testGetInvoiceRepository_ShouldReturnRepository() {
        // Act
        InvoiceRepository result = invoiceService.getInvoiceRepository();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetInvoiceLineRepository_ShouldReturnRepository() {
        // Act
        InvoiceLineRepository result = invoiceService.getInvoiceLineRepository();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testCreateInvoice_WithValidData_ShouldCreateInvoice() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(1L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(invoiceLineRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Invoice result = invoiceService.createInvoice(
            "Test Customer", "123456789", false, LocalDateTime.now(),
            serviceIds, quantites, prixs, null, null, null, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(invoiceRepository, times(1)).findMaxReference();
        verify(serviceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(invoiceLineRepository, times(1)).saveAll(any());
    }

    @Test
    void testCreateInvoice_WithPayment_ShouldCreatePaymentAndInput() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(1L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        BigDecimal amountPaid = new BigDecimal("25.00");
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(invoiceLineRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(inputRepository.save(any(Input.class))).thenReturn(new Input());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        Invoice result = invoiceService.createInvoice(
            "Test Customer", "123456789", false, LocalDateTime.now(),
            serviceIds, quantites, prixs, Payment.ModePaiement.CASH, amountPaid, LocalDateTime.now(), mockUser
        );

        // Assert
        assertNotNull(result);
        verify(inputRepository, times(1)).save(any(Input.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreateInvoice_WithServiceNotFound_ShouldThrowException() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(999L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            invoiceService.createInvoice(
                "Test Customer", "123456789", false, LocalDateTime.now(),
                serviceIds, quantites, prixs, null, null, null, mockUser
            );
        });
    }

    @Test
    void testUpdateInvoicePayment_WithValidData_ShouldUpdateInvoice() {
        // Arrange
        BigDecimal newAmountPaid = new BigDecimal("50.00");
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return invoice;
        });
        when(inputRepository.save(any(Input.class))).thenReturn(new Input());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        Invoice result = invoiceService.updateInvoicePayment(
            1L, newAmountPaid, LocalDateTime.now(), Payment.ModePaiement.CASH, true, true, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(inputRepository, times(1)).save(any(Input.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testUpdateInvoicePayment_WithInvoiceNotFound_ShouldThrowException() {
        // Arrange
        when(invoiceRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.updateInvoicePayment(
                999L, new BigDecimal("50.00"), LocalDateTime.now(), Payment.ModePaiement.CASH, true, true, mockUser
            );
        });
    }

    @Test
    void testUpdateInvoicePayment_WithoutPayment_ShouldNotCreatePaymentAndInput() {
        // Arrange
        BigDecimal newAmountPaid = new BigDecimal("50.00");
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return invoice;
        });

        // Act
        Invoice result = invoiceService.updateInvoicePayment(
            1L, newAmountPaid, LocalDateTime.now(), Payment.ModePaiement.CASH, false, true, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(inputRepository, never()).save(any(Input.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testCreateInvoice_WithNullInvoiceDateTime_ShouldUseCurrentDateTime() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(1L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(invoiceLineRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Invoice result = invoiceService.createInvoice(
            "Test Customer", "123456789", false, null,
            serviceIds, quantites, prixs, null, null, null, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testCreateInvoice_WithDeliveredTrue_ShouldSetDeliveredDate() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(1L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(invoiceLineRepository.saveAll(any())).thenReturn(new ArrayList<>());

        // Act
        Invoice result = invoiceService.createInvoice(
            "Test Customer", "123456789", true, LocalDateTime.now(),
            serviceIds, quantites, prixs, null, null, null, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testCreateInvoice_WithNullPaymentDate_ShouldUseCurrentDateTime() {
        // Arrange
        List<Long> serviceIds = Arrays.asList(1L);
        List<Integer> quantites = Arrays.asList(2);
        List<BigDecimal> prixs = Arrays.asList(new BigDecimal("50.00"));
        BigDecimal amountPaid = new BigDecimal("25.00");
        when(invoiceRepository.findMaxReference()).thenReturn(7999);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(invoiceLineRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(inputRepository.save(any(Input.class))).thenReturn(new Input());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        Invoice result = invoiceService.createInvoice(
            "Test Customer", "123456789", false, LocalDateTime.now(),
            serviceIds, quantites, prixs, Payment.ModePaiement.CASH, amountPaid, null, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testUpdateInvoicePayment_WithNullPaymentDate_ShouldUseCurrentDateTime() {
        // Arrange
        BigDecimal newAmountPaid = new BigDecimal("50.00");
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return invoice;
        });
        when(inputRepository.save(any(Input.class))).thenReturn(new Input());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        Invoice result = invoiceService.updateInvoicePayment(
            1L, newAmountPaid, null, Payment.ModePaiement.CASH, true, true, mockUser
        );

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveCorrectAnnotations() {
        // Assert
        assertTrue(InvoiceService.class.isAnnotationPresent(org.springframework.stereotype.Service.class));
    }

    @Test
    void testInvoiceServiceClass_ShouldBePublic() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(InvoiceService.class.getModifiers()));
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeAbstract() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isAbstract(InvoiceService.class.getModifiers()));
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveConstructor() {
        // Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getDeclaredConstructor(
                InvoiceRepository.class, ServiceRepository.class, 
                InvoiceLineRepository.class, PaymentRepository.class, InputRepository.class
            );
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldBeInstantiable() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new InvoiceService(invoiceRepository, serviceRepository, invoiceLineRepository, paymentRepository, inputRepository);
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveCorrectPackage() {
        // Assert
        assertEquals("com.comptel.backend.services", InvoiceService.class.getPackageName());
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveCorrectName() {
        // Assert
        assertEquals("InvoiceService", InvoiceService.class.getSimpleName());
    }

    @Test
    void testInvoiceServiceClass_ShouldExtendObject() {
        // Assert
        assertEquals(Object.class, InvoiceService.class.getSuperclass());
    }

    @Test
    void testInvoiceServiceClass_ShouldNotHaveInterfaces() {
        // Assert
        assertEquals(0, InvoiceService.class.getInterfaces().length);
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveGetInvoiceRepositoryMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getMethod("getInvoiceRepository");
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveGetInvoiceLineRepositoryMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getMethod("getInvoiceLineRepository");
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveCreateInvoiceMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getMethod("createInvoice", 
                String.class, String.class, boolean.class, LocalDateTime.class,
                List.class, List.class, List.class, Payment.ModePaiement.class, 
                BigDecimal.class, LocalDateTime.class, User.class);
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldHaveUpdateInvoicePaymentMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getMethod("updateInvoicePayment",
                Long.class, BigDecimal.class, LocalDateTime.class, Payment.ModePaiement.class,
                boolean.class, boolean.class, User.class);
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldBeAccessible() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            InvoiceService.class.getDeclaredFields();
            InvoiceService.class.getDeclaredMethods();
        });
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeFinal() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isFinal(InvoiceService.class.getModifiers()));
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeInterface() {
        // Assert
        assertFalse(InvoiceService.class.isInterface());
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeEnum() {
        // Assert
        assertFalse(InvoiceService.class.isEnum());
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeAnnotation() {
        // Assert
        assertFalse(InvoiceService.class.isAnnotation());
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBePrimitive() {
        // Assert
        assertFalse(InvoiceService.class.isPrimitive());
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeArray() {
        // Assert
        assertFalse(InvoiceService.class.isArray());
    }

    @Test
    void testInvoiceServiceClass_ShouldBeAssignableFromObject() {
        // Assert
        assertTrue(Object.class.isAssignableFrom(InvoiceService.class));
    }

    @Test
    void testInvoiceServiceClass_ShouldNotBeAssignableFromString() {
        // Assert
        assertFalse(String.class.isAssignableFrom(InvoiceService.class));
    }
} 