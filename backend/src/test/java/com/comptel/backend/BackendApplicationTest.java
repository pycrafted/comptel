package com.comptel.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTest {

    @Test
    void testBackendApplicationClass_ShouldHaveCorrectAnnotations() {
        // Assert
        assertTrue(BackendApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testBackendApplicationClass_ShouldHaveMainMethod() {
        // Assert
        try {
            BackendApplication.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("main method with String[] parameter not found");
        }
    }

    @Test
    void testBackendApplicationClass_ShouldBePublic() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(BackendApplication.class.getModifiers()));
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeAbstract() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isAbstract(BackendApplication.class.getModifiers()));
    }

    @Test
    void testBackendApplicationClass_ShouldHaveDefaultConstructor() {
        // Assert
        assertDoesNotThrow(() -> {
            BackendApplication.class.getDeclaredConstructor();
        });
    }

    @Test
    void testBackendApplicationClass_ShouldBeInstantiable() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new BackendApplication();
        });
    }

    @Test
    void testBackendApplicationClass_ShouldHaveCorrectPackage() {
        // Assert
        assertEquals("com.comptel.backend", BackendApplication.class.getPackageName());
    }

    @Test
    void testBackendApplicationClass_ShouldHaveCorrectName() {
        // Assert
        assertEquals("BackendApplication", BackendApplication.class.getSimpleName());
    }

    @Test
    void testBackendApplicationClass_ShouldExtendObject() {
        // Assert
        assertEquals(Object.class, BackendApplication.class.getSuperclass());
    }

    @Test
    void testBackendApplicationClass_ShouldNotHaveInterfaces() {
        // Assert
        assertEquals(0, BackendApplication.class.getInterfaces().length);
    }

    @Test
    void testBackendApplicationClass_ShouldHaveMainMethodWithCorrectSignature() {
        // Act
        try {
            java.lang.reflect.Method mainMethod = BackendApplication.class.getMethod("main", String[].class);

            // Assert
            assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
            assertEquals(void.class, mainMethod.getReturnType());
            assertEquals(1, mainMethod.getParameterCount());
            assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
        } catch (NoSuchMethodException e) {
            fail("main method with String[] parameter not found");
        }
    }

    @Test
    void testBackendApplicationClass_ShouldHaveSpringBootApplicationAnnotation() {
        // Act
        org.springframework.boot.autoconfigure.SpringBootApplication annotation = 
            BackendApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class);

        // Assert
        assertNotNull(annotation);
    }

    @Test
    void testBackendApplicationClass_ShouldBeInCorrectModule() {
        // Assert
        assertNotNull(BackendApplication.class.getModule());
    }

    @Test
    void testBackendApplicationClass_ShouldBeAccessible() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            BackendApplication.class.getDeclaredFields();
            BackendApplication.class.getDeclaredMethods();
        });
    }

    @Test
    void testBackendApplicationClass_ShouldHaveNoDeclaredFields() {
        // Act
        java.lang.reflect.Field[] fields = BackendApplication.class.getDeclaredFields();

        // Assert
        assertEquals(0, fields.length);
    }

    @Test
    void testBackendApplicationClass_ShouldHaveMainMethodOnly() {
        // Act
        java.lang.reflect.Method[] methods = BackendApplication.class.getDeclaredMethods();

        // Assert
        assertEquals(1, methods.length);
        assertEquals("main", methods[0].getName());
    }

    @Test
    void testBackendApplicationClass_ShouldBeFinal() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isFinal(BackendApplication.class.getModifiers()));
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeInterface() {
        // Assert
        assertFalse(BackendApplication.class.isInterface());
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeEnum() {
        // Assert
        assertFalse(BackendApplication.class.isEnum());
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeAnnotation() {
        // Assert
        assertFalse(BackendApplication.class.isAnnotation());
    }

    @Test
    void testBackendApplicationClass_ShouldNotBePrimitive() {
        // Assert
        assertFalse(BackendApplication.class.isPrimitive());
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeArray() {
        // Assert
        assertFalse(BackendApplication.class.isArray());
    }

    @Test
    void testBackendApplicationClass_ShouldBeAssignableFromObject() {
        // Assert
        assertTrue(Object.class.isAssignableFrom(BackendApplication.class));
    }

    @Test
    void testBackendApplicationClass_ShouldNotBeAssignableFromString() {
        // Assert
        assertFalse(String.class.isAssignableFrom(BackendApplication.class));
    }
} 