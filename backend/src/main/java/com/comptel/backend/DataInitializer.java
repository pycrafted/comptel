package com.comptel.backend;

import com.comptel.backend.entity.User;
import com.comptel.backend.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Cette classe permet de creer automatiquement un utilisateur comme username: admin et password: admin
 */
@Configuration
public class DataInitializer {

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
            // Check if the users table is empty
            if (userRepository.count() == 0) {
                // Create admin user with encoded password
                User admin = new User("admin", passwordEncoder.encode("admin"),true);
                userRepository.save(admin);
                System.out.println("Default admin user created: username=admin, password=admin");
            } else {
                System.out.println("Users table is not empty, skipping admin user creation.");
            }
        };
    }
}
