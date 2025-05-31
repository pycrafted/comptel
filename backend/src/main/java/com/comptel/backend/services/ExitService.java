package com.comptel.backend.services;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.entity.Depense;
import com.comptel.backend.entity.User;
import com.comptel.backend.repository.ExitRepository;
import com.comptel.backend.repository.DepenseRepository;
import com.comptel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExitService {
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
        Exit exit = new Exit();
        exit.setTitre(titre);
        exit.setMontant(montant);
        exit.setTypeDepense(typeDepense);
        exit.setCreatedAt(LocalDateTime.now());

        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(exit::setSaveBy);

        exit = exitRepository.save(exit);

        // Création automatique d'une dépense associée si un type est spécifié
        if (typeDepense != null) {
            Depense depense = new Depense();
            depense.setType(typeDepense);
            depense.setIntitule(titre);
            depense.setMontant(montant);
            depense.setQuantite(1);
            depense.setDateDepense(LocalDateTime.now());
            depenseRepository.save(depense);
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