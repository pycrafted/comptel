package com.comptel.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Depense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Exit.TypeDepense type;

    private String intitule;
    private BigDecimal montant = BigDecimal.ZERO;
    private Integer quantite = 1;
    private LocalDateTime dateDepense = LocalDateTime.now();

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exit.TypeDepense getType() {
        return type;
    }

    public void setType(Exit.TypeDepense type) {
        this.type = type;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    @Override
    public String toString() {
        return String.format("%s - %s x %dF.FCFA", intitule, montant, quantite);
    }
}