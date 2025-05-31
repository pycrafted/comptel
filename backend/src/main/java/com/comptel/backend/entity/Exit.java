package com.comptel.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Exit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private BigDecimal montant = BigDecimal.ZERO;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User saveBy;

    public enum TypeDepense {
        RETRAIT("Retrait"),
        REPARATION("Réparation"),
        SALAIRE("Salaire"),
        FACTURE_EAU("Facture eau"),
        ELECTRICITE("Électricité"),
        PRODUIT_REPASSAGE("Produit repassage"),
        PRODUIT_LAVAGE("Produit lavage"),
        FRAIS_DIVERS("Frais divers");

        private final String label;

        TypeDepense(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    @Enumerated(EnumType.STRING)
    private TypeDepense typeDepense;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getSaveBy() {
        return saveBy;
    }

    public void setSaveBy(User saveBy) {
        this.saveBy = saveBy;
    }

    public TypeDepense getTypeDepense() {
        return typeDepense;
    }

    public void setTypeDepense(TypeDepense typeDepense) {
        this.typeDepense = typeDepense;
    }
}