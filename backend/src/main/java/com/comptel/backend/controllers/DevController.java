package com.comptel.backend.controllers;

import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
public class DevController {
    private final ServiceRepository serviceRepository;

    public DevController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @PostMapping("/populate")
    public ResponseEntity<String> populateTestData() {
        // Exemple : Ajout de services fictifs
        Service s1 = new Service();
        s1.setDesignation("Service Test 1");
        s1.setProposition("Proposition 1");
        s1.setPrix(new java.math.BigDecimal("100.0"));
        serviceRepository.save(s1);

        Service s2 = new Service();
        s2.setDesignation("Service Test 2");
        s2.setProposition("Proposition 2");
        s2.setPrix(new java.math.BigDecimal("200.0"));
        serviceRepository.save(s2);
        // À enrichir avec d'autres entités (utilisateurs, factures, etc.)
        return ResponseEntity.ok("Données de test insérées (services)");
    }
} 