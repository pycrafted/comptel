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
}