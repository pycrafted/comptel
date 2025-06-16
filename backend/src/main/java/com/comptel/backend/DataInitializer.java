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
            logger.info("Vérification de l'initialisation des données...");
            long userCount = userRepository.count();
            logger.info("Nombre d'utilisateurs dans la base: {}", userCount);

            if (userCount == 0) {
                logger.info("Création de l'utilisateur admin par défaut...");
                String encodedPassword = passwordEncoder.encode("admin");
                logger.debug("Mot de passe encodé: {}", encodedPassword);
                
                User admin = new User("admin", encodedPassword, true);
                userRepository.save(admin);
                logger.info("Utilisateur admin créé avec succès");
            } else {
                logger.info("La table users n'est pas vide, mise à jour du mot de passe admin...");
                // Mettre à jour le mot de passe de l'utilisateur admin
                userRepository.findByUsername("admin").ifPresent(admin -> {
                    String encodedPassword = passwordEncoder.encode("admin");
                    admin.setPassword(encodedPassword);
                    userRepository.save(admin);
                    logger.info("Mot de passe admin mis à jour avec succès");
                });
                
                // Afficher les utilisateurs existants
                userRepository.findAll().forEach(user -> 
                    logger.info("Utilisateur existant: {}, rôle: {}", 
                        user.getUsername(), 
                        user.isRole() ? "ADMIN" : "USER")
                );
            }
        };
    }
}
