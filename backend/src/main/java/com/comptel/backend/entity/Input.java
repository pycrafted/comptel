package com.comptel.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Input {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titres; // correspond à titres

    private BigDecimal montants; // correspond à montants

    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement; // correspond à mode_paiement

    private LocalDateTime createdAts; // correspond à created_ats

    @ManyToOne
    private User saveBy; // correspond à save_by

    public enum ModePaiement {
        cash, om, wave
    }

    @PrePersist
    public void prePersist() {
        if (createdAts == null) {
            createdAts = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitres() {
        return titres;
    }

    public void setTitres(String titres) {
        this.titres = titres;
    }

    public BigDecimal getMontants() {
        return montants;
    }

    public void setMontants(BigDecimal montants) {
        this.montants = montants;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public LocalDateTime getCreatedAts() {
        return createdAts;
    }

    public void setCreatedAts(LocalDateTime createdAts) {
        this.createdAts = createdAts;
    }

    public User getSaveBy() {
        return saveBy;
    }

    public void setSaveBy(User saveBy) {
        this.saveBy = saveBy;
    }
}
