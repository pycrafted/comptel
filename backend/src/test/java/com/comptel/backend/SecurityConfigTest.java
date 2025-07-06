package com.comptel.backend;

import com.comptel.backend.services.JwtService;
import com.comptel.backend.services.UseImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UseImpl useImpl;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testSecurityConfigBeanCreation() {
        // Assert
        assertNotNull(securityConfig);
        assertNotNull(jwtService);
        assertNotNull(useImpl);
    }

    @Test
    public void testPasswordEncoderBean() {
        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testPasswordEncoderFunctionality() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testpassword";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongpassword", encodedPassword));
    }

    @Test
    public void testPasswordEncoderMultipleEncodings() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testpassword";

        // Act
        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotEquals(encoded1, encoded2); // Each encoding should be different due to salt
        assertTrue(passwordEncoder.matches(rawPassword, encoded1));
        assertTrue(passwordEncoder.matches(rawPassword, encoded2));
    }

    @Test
    public void testPasswordEncoderWithSpecialCharacters() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "p@ssw0rd!@#$%^&*()";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testPasswordEncoderWithEmptyPassword() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testPasswordEncoderWithNullPassword() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.encode(null);
        });
    }

    @Test
    public void testPasswordEncoderWithVeryLongPassword() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "a".repeat(1000);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            passwordEncoder.encode(rawPassword);
        });
    }

    @Test
    public void testSecurityConfigClassAnnotations() {
        // Assert
        assertTrue(SecurityConfig.class.isAnnotationPresent(EnableWebSecurity.class));
    }

    @Test
    public void testSecurityConfigMethodsExist() {
        // Test that the required methods exist
        assertDoesNotThrow(() -> {
            SecurityConfig.class.getMethod("passwordEncoder");
        });
    }

    @Test
    public void testPasswordEncoderConsistency() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "consistentpassword";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert - multiple matches should work consistently
        for (int i = 0; i < 10; i++) {
            assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        }
    }

    @Test
    public void testPasswordEncoderWithUnicodeCharacters() {
        // Arrange
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "motdepasseéàçù€£¥";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testSecurityFilterChainBeanExistsInContext() {
        // Act
        String[] beanNames = applicationContext.getBeanNamesForType(SecurityFilterChain.class);

        // Assert
        assertTrue(beanNames.length > 0, "A SecurityFilterChain bean should exist in the context");
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            assertNotNull(bean);
            assertTrue(bean instanceof SecurityFilterChain);
        }
    }
} 