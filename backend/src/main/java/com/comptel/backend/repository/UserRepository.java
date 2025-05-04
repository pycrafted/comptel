package com.comptel.backend.repository;

import com.comptel.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // Ajouté pour chercher par nom d’utilisateur
}