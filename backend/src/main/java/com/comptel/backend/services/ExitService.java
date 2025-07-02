package com.comptel.backend.services;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.entity.Depense;
import com.comptel.backend.entity.User;
import com.comptel.backend.repository.ExitRepository;
import com.comptel.backend.repository.DepenseRepository;
import com.comptel.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExitService {
    private static final Logger logger = LoggerFactory.getLogger(ExitService.class);
    private final ExitRepository exitRepository;
    private final DepenseRepository depenseRepository;
    private final UserRepository userRepository;

    public ExitService(ExitRepository exitRepository, DepenseRepository depenseRepository, UserRepository userRepository) {
        this.exitRepository = exitRepository;
        this.depenseRepository = depenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Exit createExit(String titre, BigDecimal montant, Exit.TypeDepense typeDepense, Long userId) {
        logger.info("Début de la création d'une sortie - titre={}, montant={}, typeDepense={}, userId={}", 
            titre, montant, typeDepense, userId);

        Exit exit = new Exit();
        exit.setTitre(titre);
        exit.setMontant(montant);
        exit.setTypeDepense(typeDepense);
        exit.setCreatedAt(LocalDateTime.now());
        logger.debug("Sortie créée avec les données de base");

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            logger.debug("Utilisateur trouvé: {}", user.get().getUsername());
            exit.setSaveBy(user.get());
        } else {
            logger.warn("Utilisateur non trouvé avec l'ID: {}", userId);
        }

        exit = exitRepository.save(exit);
        logger.info("Sortie enregistrée avec l'ID: {}", exit.getId());

        // Création automatique d'une dépense associée si un type est spécifié
        if (typeDepense != null) {
            logger.debug("Création de la dépense associée");
            Depense depense = new Depense();
            depense.setType(typeDepense);
            depense.setIntitule(titre);
            depense.setMontant(montant);
            depense.setQuantite(1);
            depense.setDateDepense(LocalDateTime.now());
            depenseRepository.save(depense);
            logger.info("Dépense associée créée avec succès");
        }

        return exit;
    }

    @Transactional
    public Exit updateExit(Long id, String titre, BigDecimal montant, Exit.TypeDepense typeDepense) {
        Exit exit = exitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sortie non trouvée : " + id));

        exit.setTitre(titre);
        exit.setMontant(montant);
        exit.setTypeDepense(typeDepense);

        return exitRepository.save(exit);
    }

    public void deleteExit(Long id) {
        Exit exit = exitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sortie non trouvée : " + id));
        exitRepository.delete(exit);
    }

    public Exit findById(Long id) {
        return exitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sortie non trouvée : " + id));
    }

    public List<Exit> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return exitRepository.findByCreatedAtBetween(start, end);
    }

    public List<Exit> findByType(Exit.TypeDepense typeDepense) {
        return exitRepository.findByTypeDepense(typeDepense);
    }

    public ExitRepository getExitRepository() {
        return exitRepository;
    }
}