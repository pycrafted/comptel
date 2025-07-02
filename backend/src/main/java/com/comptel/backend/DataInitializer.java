package com.comptel.backend;

import com.comptel.backend.entity.User;
import com.comptel.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Cette classe permet de creer automatiquement un utilisateur comme username: admin et password: admin
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Configure un ApplicationRunner pour créer un utilisateur administrateur par défaut au démarrage de l'application
     * si la table users est vide.
     * @param userRepository pour interagir avec la table users dans la base de données.
     * @param passwordEncoder pour hacher le mot de passe de l'utilisateur.
     * @return
     */
    @Bean
    public ApplicationRunner initializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                // Check if admin user already exists
                if (userRepository.findByUsername("admin").isEmpty()) {
                    // Create admin user with encoded password
                    User admin = new User("admin", passwordEncoder.encode("admin"), true);
                    userRepository.save(admin);
                    logger.info("✅ Default admin user created successfully!");
                    logger.info("📝 Username: admin");
                    logger.info("🔑 Password: admin");
                    logger.info("⚠️  IMPORTANT: Change this password after first login!");
                } else {
                    logger.info("ℹ️  Admin user already exists, skipping creation.");
                }
            } catch (Exception e) {
                logger.error("❌ Error creating admin user: " + e.getMessage(), e);
                // Don't fail the application startup if user creation fails
            }
        };
    }
}
