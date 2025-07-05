//package com.comptel.backend.services;
//
//import com.comptel.backend.entity.Input;
//import com.comptel.backend.entity.User;
//import com.comptel.backend.repository.InputRepository;
//import com.comptel.backend.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//
//@Service
//public class InputService {
//    private final InputRepository inputRepository;
//    private final UserRepository userRepository;
//
//    public InputService(InputRepository inputRepository, UserRepository userRepository) {
//        this.inputRepository = inputRepository;
//        this.userRepository = userRepository;
//
//    }
//
//    public Input createInput(String titres, BigDecimal montants, String modePaiement, Long userId) {
//        Input input = new Input();
//        input.setTitres(titres);
//        input.setMontants(montants);
//        input.setModePaiement(Input.ModePaiement.valueOf(modePaiement));
//        input.setCreatedAts(LocalDateTime.now());
//
//        Optional<User> user = userRepository.findById(userId);
//        user.ifPresent(input::setSaveBy);
//
//        return inputRepository.save(input);
//    }
//
//    public Input updateInput(Long id, String titres, BigDecimal montants, String modePaiement) {
//        Input input = inputRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));
//        input.setTitres(titres);
//        input.setMontants(montants);
//        input.setModePaiement(Input.ModePaiement.valueOf(modePaiement));
//        return inputRepository.save(input);
//    }
//
//    public void deleteInput(Long id) {
//        Input input = inputRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));
//        inputRepository.delete(input);
//    }
//
//    public Input findById(Long id) {
//        return inputRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));
//    }
//
//    public InputRepository getInputRepository() {
//        return inputRepository;
//    }
//}

package com.comptel.backend.services;

import com.comptel.backend.entity.Input;
import com.comptel.backend.entity.User;
import com.comptel.backend.repository.InputRepository;
import com.comptel.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InputService {
    private final InputRepository inputRepository;
    private final UserRepository userRepository;

    public InputService(InputRepository inputRepository, UserRepository userRepository) {
        this.inputRepository = inputRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Input createInput(String titres, BigDecimal montants, String modePaiement, Long userId) {
        validateAmount(montants);

        Input input = new Input();
        input.setTitres(titres);
        input.setMontants(montants);
        input.setModePaiement(Input.ModePaiement.valueOf(modePaiement.toLowerCase()));
        input.setCreatedAts(LocalDateTime.now());

        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(input::setSaveBy);

        return inputRepository.save(input);
    }

    @Transactional
    public Input updateInput(Long id, String titres, BigDecimal montants, String modePaiement) {
        validateAmount(montants);

        Input input = inputRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));

        input.setTitres(titres);
        input.setMontants(montants);
        input.setModePaiement(Input.ModePaiement.valueOf(modePaiement.toLowerCase()));

        return inputRepository.save(input);
    }

    @Transactional
    public void deleteInput(Long id) {
        Input input = inputRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));
        inputRepository.delete(input);
    }

    public Input findById(Long id) {
        return inputRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Input non trouvé : " + id));
    }

    public List<Input> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return inputRepository.findByCreatedAtsBetween(start, end);
    }

    public List<Input> findByModePaiement(Input.ModePaiement modePaiement) {
        return inputRepository.findByModePaiement(modePaiement);
    }

    public List<Input> findByUserId(Long userId) {
        return inputRepository.findBySaveBy_Id(userId);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif");
        }
    }

    public InputRepository getInputRepository() {
        return inputRepository;
    }
}