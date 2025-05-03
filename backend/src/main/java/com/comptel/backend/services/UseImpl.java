package com.comptel.backend.services;

import com.comptel.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Ce service implémente UserDetailsService pour charger les détails des utilisateurs depuis la base de données.
 */
@Service
public class UseImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Initialise le service avec un repository pour accéder à la base de données.
     * @param userRepository
     */
    public UseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge les détails d'un utilisateur à partir de son nom d'utilisateur.
     * @param username
     * @return Objet contenant le username, le mot de passe haché, et les rôles.
     * @throws UsernameNotFoundException : Si l'utilisateur n'existe pas.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.comptel.backend.entity.User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            com.comptel.backend.entity.User currentUser = user.get();
            return User.withUsername(username).password(currentUser.getPassword()).roles(currentUser.isRole()? "admin" : "user")
                    .build();
        } else {
            throw new UsernameNotFoundException("Utilisateur non trouvé.");
        }
    }
}
