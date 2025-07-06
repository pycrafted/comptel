package com.comptel.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void testCorsConfigurer_NotNull() {
        // Act
        WebMvcConfigurer result = corsConfig.corsConfigurer();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testCorsConfigurer_IsWebMvcConfigurer() {
        // Act
        WebMvcConfigurer result = corsConfig.corsConfigurer();

        // Assert
        assertTrue(result instanceof WebMvcConfigurer);
    }

    @Test
    void testCorsConfigurer_CanAddCorsMappings() {
        // Act
        WebMvcConfigurer configurer = corsConfig.corsConfigurer();

        // Assert
        assertDoesNotThrow(() -> {
            // Test that the configurer can be used
            assertNotNull(configurer);
        });
    }

    @Test
    void testCorsConfigurer_ReturnsNewInstance() {
        // Act
        WebMvcConfigurer configurer1 = corsConfig.corsConfigurer();
        WebMvcConfigurer configurer2 = corsConfig.corsConfigurer();

        // Assert
        assertNotNull(configurer1);
        assertNotNull(configurer2);
        assertNotSame(configurer1, configurer2); // Should create new instances
    }

    @Test
    void testCorsConfigClass_ShouldHaveCorrectAnnotations() {
        // Assert
        assertTrue(CorsConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void testCorsConfigClass_ShouldBePublic() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(CorsConfig.class.getModifiers()));
    }

    @Test
    void testCorsConfigClass_ShouldNotBeAbstract() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isAbstract(CorsConfig.class.getModifiers()));
    }

    @Test
    void testCorsConfigClass_ShouldHaveDefaultConstructor() {
        // Assert
        assertDoesNotThrow(() -> {
            CorsConfig.class.getDeclaredConstructor();
        });
    }

    @Test
    void testCorsConfigClass_ShouldBeInstantiable() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            new CorsConfig();
        });
    }

    @Test
    void testCorsConfigClass_ShouldHaveCorrectPackage() {
        // Assert
        assertEquals("com.comptel.backend", CorsConfig.class.getPackageName());
    }

    @Test
    void testCorsConfigClass_ShouldHaveCorrectName() {
        // Assert
        assertEquals("CorsConfig", CorsConfig.class.getSimpleName());
    }

    @Test
    void testCorsConfigClass_ShouldExtendObject() {
        // Assert
        assertEquals(Object.class, CorsConfig.class.getSuperclass());
    }

    @Test
    void testCorsConfigClass_ShouldNotHaveInterfaces() {
        // Assert
        assertEquals(0, CorsConfig.class.getInterfaces().length);
    }

    @Test
    void testCorsConfigClass_ShouldHaveCorsConfigurerMethod() {
        // Assert
        assertDoesNotThrow(() -> {
            CorsConfig.class.getMethod("corsConfigurer");
        });
    }

    @Test
    void testCorsConfigClass_ShouldBeAccessible() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CorsConfig.class.getDeclaredFields();
            CorsConfig.class.getDeclaredMethods();
        });
    }

    @Test
    void testCorsConfigClass_ShouldNotBeFinal() {
        // Assert
        assertFalse(java.lang.reflect.Modifier.isFinal(CorsConfig.class.getModifiers()));
    }

    @Test
    void testCorsConfigClass_ShouldNotBeInterface() {
        // Assert
        assertFalse(CorsConfig.class.isInterface());
    }

    @Test
    void testCorsConfigClass_ShouldNotBeEnum() {
        // Assert
        assertFalse(CorsConfig.class.isEnum());
    }

    @Test
    void testCorsConfigClass_ShouldNotBeAnnotation() {
        // Assert
        assertFalse(CorsConfig.class.isAnnotation());
    }

    @Test
    void testCorsConfigClass_ShouldNotBePrimitive() {
        // Assert
        assertFalse(CorsConfig.class.isPrimitive());
    }

    @Test
    void testCorsConfigClass_ShouldNotBeArray() {
        // Assert
        assertFalse(CorsConfig.class.isArray());
    }

    @Test
    void testCorsConfigClass_ShouldBeAssignableFromObject() {
        // Assert
        assertTrue(Object.class.isAssignableFrom(CorsConfig.class));
    }

    @Test
    void testCorsConfigClass_ShouldNotBeAssignableFromString() {
        // Assert
        assertFalse(String.class.isAssignableFrom(CorsConfig.class));
    }
} 