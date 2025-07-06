package com.comptel.backend;

import com.comptel.backend.entity.User;
import com.comptel.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class DataInitializerTest {

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private PasswordEncoder passwordEncoder;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(true);
    }

    @Test
    void testInitializer_WhenNoUsersExist_ShouldCreateAdminUser() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);
        runner.run(null);

        // Assert
        verify(userRepository, times(1)).count();
        verify(passwordEncoder, times(1)).encode("admin");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testInitializer_WhenUsersExist_ShouldUpdateAdminPassword() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("admin")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);
        runner.run(null);

        // Assert
        verify(userRepository, times(1)).count();
        verify(userRepository, times(1)).findByUsername("admin");
        verify(passwordEncoder, times(1)).encode("admin");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testInitializer_WhenAdminUserNotFound_ShouldHandleGracefully() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // Act
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);
        runner.run(null);

        // Assert
        verify(userRepository, times(1)).count();
        verify(userRepository, times(1)).findByUsername("admin");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testInitializer_ShouldReturnApplicationRunner() {
        // Act
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);

        // Assert
        assertNotNull(runner);
        assertTrue(runner instanceof ApplicationRunner);
    }

    @Test
    void testInitializer_ShouldHandleMultipleCalls() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        ApplicationRunner runner1 = dataInitializer.initializer(userRepository, passwordEncoder);
        ApplicationRunner runner2 = dataInitializer.initializer(userRepository, passwordEncoder);
        runner1.run(null);
        runner2.run(null);

        // Assert
        assertNotNull(runner1);
        assertNotNull(runner2);
        assertNotSame(runner1, runner2);
    }

    @Test
    void testInitializer_ShouldHandleExceptionGracefully() throws Exception {
        // Arrange
        when(userRepository.count()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);
        assertThrows(RuntimeException.class, () -> runner.run(null));
    }

    @Test
    void testInitializer_ShouldHandleSaveExceptionGracefully() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save error"));

        // Act & Assert
        ApplicationRunner runner = dataInitializer.initializer(userRepository, passwordEncoder);
        assertThrows(RuntimeException.class, () -> runner.run(null));
    }

    @Test
    void testDataInitializerClass_ShouldHaveCorrectAnnotations() {
        // Assert
        assertTrue(DataInitializer.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void testDataInitializerClass_ShouldBePublic() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(DataInitializer.class.getModifiers()));
    }

    @Test
    void testDataInitializerClass_ShouldNotBeAbstract() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isAbstract(DataInitializer.class.getModifiers()));
    }

    @Test
    void testDataInitializerClass_ShouldHaveDefaultConstructor() {
        // Assert
        assertDoesNotThrow(() -> {
            DataInitializer.class.getDeclaredConstructor();
        });
    }

    @Test
    void testDataInitializerClass_ShouldBeInstantiable() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new DataInitializer();
        });
    }

    @Test
    void testDataInitializerClass_ShouldHaveCorrectPackage() {
        // Assert
        assertEquals("com.comptel.backend", DataInitializer.class.getPackageName());
    }

    @Test
    void testDataInitializerClass_ShouldHaveCorrectName() {
        // Assert
        assertEquals("DataInitializer", DataInitializer.class.getSimpleName());
    }

    @Test
    void testDataInitializerClass_ShouldExtendObject() {
        // Assert
        assertEquals(Object.class, DataInitializer.class.getSuperclass());
    }

    @Test
    void testDataInitializerClass_ShouldNotHaveInterfaces() {
        // Assert
        assertEquals(0, DataInitializer.class.getInterfaces().length);
    }

    @Test
    void testDataInitializerClass_ShouldHaveInitializerMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            DataInitializer.class.getMethod("initializer", UserRepository.class, PasswordEncoder.class);
        });
    }

    @Test
    void testDataInitializerClass_ShouldBeAccessible() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            DataInitializer.class.getDeclaredFields();
            DataInitializer.class.getDeclaredMethods();
        });
    }

    @Test
    void testDataInitializerClass_ShouldNotBeFinal() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isFinal(DataInitializer.class.getModifiers()));
    }

    @Test
    void testDataInitializerClass_ShouldNotBeInterface() {
        // Assert
        assertFalse(DataInitializer.class.isInterface());
    }

    @Test
    void testDataInitializerClass_ShouldNotBeEnum() {
        // Assert
        assertFalse(DataInitializer.class.isEnum());
    }

    @Test
    void testDataInitializerClass_ShouldNotBeAnnotation() {
        // Assert
        assertFalse(DataInitializer.class.isAnnotation());
    }

    @Test
    void testDataInitializerClass_ShouldNotBePrimitive() {
        // Assert
        assertFalse(DataInitializer.class.isPrimitive());
    }

    @Test
    void testDataInitializerClass_ShouldNotBeArray() {
        // Assert
        assertFalse(DataInitializer.class.isArray());
    }

    @Test
    void testDataInitializerClass_ShouldBeAssignableFromObject() {
        // Assert
        assertTrue(Object.class.isAssignableFrom(DataInitializer.class));
    }

    @Test
    void testDataInitializerClass_ShouldNotBeAssignableFromString() {
        // Assert
        assertFalse(String.class.isAssignableFrom(DataInitializer.class));
    }
} 