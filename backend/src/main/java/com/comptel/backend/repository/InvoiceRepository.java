package com.comptel.backend.repository;

import com.comptel.backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /*
    La requete permet de recuperer la valeur la plus grande dans la table invoice pour la colone reference.
    Permettant ainsi de recuperer le dernier facture qui a ete ajouter recenment.
    findMaxReference() est une foncction qui retourne un entier et la valeur provient de la requete.
     */
    @Query("SELECT MAX(i.reference) FROM Invoice i")
    Integer findMaxReference();

    List<Invoice> findByInvoiceDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Récupère toutes les factures avec leurs services chargés en une seule requête.
     * Utilise un fetch join pour éviter les problèmes de lazy loading.
     */
    @Query("SELECT DISTINCT i FROM Invoice i " +
           "LEFT JOIN FETCH i.invoiceServices " +
           "ORDER BY i.invoiceDateTime DESC")
    List<Invoice> findAllWithServices();

    /**
     * Récupère les 5 dernières factures ordonnées par date de création décroissante.
     */
    @Query("SELECT i FROM Invoice i ORDER BY i.invoiceDateTime DESC LIMIT 5")
    List<Invoice> findTop5ByOrderByInvoiceDateTimeDesc();
}