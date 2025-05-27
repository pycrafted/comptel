package com.comptel.backend.services;

import com.comptel.backend.entity.*;
import com.comptel.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InputRepository inputRepository;
    private final ServiceRepository serviceRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final PaymentRepository paymentRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, ServiceRepository serviceRepository,
                          InvoiceLineRepository invoiceLineRepository, PaymentRepository paymentRepository,
                            InputRepository inputRepository) {
        this.invoiceRepository = invoiceRepository;
        this.serviceRepository = serviceRepository;
        this.invoiceLineRepository = invoiceLineRepository;
        this.paymentRepository = paymentRepository;
        this.inputRepository = inputRepository;
    }

    /**
     * Retourne le repository des factures pour permettre des opérations supplémentaires.
     *
     * @return InvoiceRepository pour accéder aux opérations sur les factures.
     */
    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
    }
    public InvoiceLineRepository getInvoiceLineRepository(){
        return invoiceLineRepository;
    }
    /**
     * Crée une nouvelle facture avec ses lignes de services et un paiement initial (si fourni).
     * La méthode est transactionnelle pour garantir la cohérence des données.
     *
     * @param customer Nom du client.
     * @param telephone Numéro de téléphone du client.
     * @param delivered Indique si la facture est livrée.
     * @param invoiceDateTime Date et heure de la facture (optionnel, utilise la date actuelle si null).
     * @param serviceIds Liste des identifiants des services inclus.
     * @param quantites Liste des quantités pour chaque service.
     * @param prixs Liste des prix unitaires pour chaque service.
     * @param modePaiement Mode de paiement initial (optionnel).
     * @param amountPaid Montant payé initialement (optionnel).
     * @param paymentDate Date du paiement initial (optionnel, utilise la date actuelle si null).
     * @param saveBy Utilisateur ayant créé la facture.
     * @return Invoice La facture créée et persistée.
     * @throws RuntimeException Si un service spécifié n'existe pas.
     */
    @Transactional
    public Invoice createInvoice(String customer, String telephone, boolean delivered, LocalDateTime invoiceDateTime,
                                 List<Long> serviceIds, List<Integer> quantites, List<BigDecimal> prixs,
                                 Payment.ModePaiement modePaiement, BigDecimal amountPaid, LocalDateTime paymentDate,
                                 User saveBy) {
        Integer lastReference = invoiceRepository.findMaxReference();
        int reference = (lastReference != null ? lastReference : 7999) + 1;

        Invoice invoice = new Invoice();
        invoice.setReference(reference);
        invoice.setCustomer(customer);
        invoice.setTelephone(telephone);
        invoice.setSaveBy(saveBy);
        invoice.setInvoiceDateTime(invoiceDateTime != null ? invoiceDateTime : LocalDateTime.now());
        invoice.setDelivered(delivered);
        if (delivered) {
            invoice.setDeliveredDate(LocalDateTime.now());
        }

        BigDecimal total = BigDecimal.ZERO;
        List<InvoiceLine> invoiceLines = new ArrayList<>();
        for (int i = 0; i < serviceIds.size(); i++) {
            final Long serviceId = serviceIds.get(i); // Variable finale pour la lambda
            com.comptel.backend.entity.Service  service = serviceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service non trouvé : " + serviceId));
            InvoiceLine invoiceLine = new InvoiceLine();
            invoiceLine.setInvoice(invoice);
            invoiceLine.setService(service);
            invoiceLine.setQuantite(quantites.get(i));
            invoiceLine.setPrix(prixs.get(i));
            invoiceLines.add(invoiceLine);
            total = total.add(prixs.get(i).multiply(BigDecimal.valueOf(quantites.get(i))));
        }

        invoice.setTotal(total);
        invoice.setAmountPaid(amountPaid);
        invoice.updateBalance();

        invoiceRepository.save(invoice);
        invoiceLineRepository.saveAll(invoiceLines);

        if (modePaiement != null && amountPaid != null && amountPaid.compareTo(BigDecimal.ZERO) > 0) {

            Payment payment = new Payment();
            Input entree = new Input();

            // Création de l'entrée de caisse correspondante
            entree.setTitres(String.valueOf(reference));
            entree.setModePaiement(Input.ModePaiement.valueOf(modePaiement.name().toLowerCase()));
            entree.setMontants(amountPaid);
            entree.setCreatedAts(LocalDateTime.now());
            entree.setSaveBy(saveBy);
            inputRepository.save(entree);

            // Enregistrement du paiement
            payment.setInvoice(invoice);
            payment.setAmount(amountPaid);
            payment.setModePaiement(Payment.ModePaiement.valueOf(modePaiement.name().toUpperCase()));
            payment.setPaymentDate(paymentDate != null ? paymentDate : LocalDateTime.now());
            paymentRepository.save(payment);

            // Mise à jour de la facture avec le paiement
            invoice.setAmountPaid(amountPaid);
            invoice.updateBalance();
            invoiceRepository.save(invoice);
        }

        return invoice;
    }

    /** Met à jour l'état du paiement et de la livraison d'une facture existante, en enregistrant de nouveaux paiements et des entrées
     de caisse.
     * Cette méthode est transactionnelle, garantissant que toutes les opérations sur la base de données (mise à jour de la facture,
     * paiement et création d'entrée) sont* exécutées de manière atomique. Elle calcule le nouveau montant total payé, met à jour le solde de
     * la facture et, le cas échéant,* enregistre un nouveau paiement et une entrée de caisse (Input) pour le montant de paiement
     * supplémentaire.
     *
     * @param invoiceId     L'ID de la facture à mettre à jour.
     * @param newAmountPaid Le montant de paiement supplémentaire à ajouter au montant déjà payé.
     * @param paymentDate   La date du nouveau paiement, ou la date actuelle si null.
     * @param modePaiement  La méthode de paiement (par exemple, espèces, om, wave).
     * @param livrer        Indique si un paiement doit être traité (mal nommé ; devrait être hasPayment).
     * @param paiement      Indique si la facture est livrée (mal nommé ; devrait être delivered).
     * @param saveBy        L'utilisateur effectuant la mise à jour.
     * @return L'objet Facture mis à jour.
     * @throws IllegalArgumentException Si la facture n'est pas trouvée.
     * @throws RuntimeException  Si le mode de paiement est invalide ou si les contraintes de la base de données sont violées.
     */
    @Transactional
    public Invoice updateInvoicePayment(Long invoiceId, BigDecimal newAmountPaid, LocalDateTime paymentDate,
                                        Payment.ModePaiement modePaiement, boolean livrer, boolean paiement,
                                        User saveBy) {
        // 1. Récupérer la facture
        Invoice facture = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Facture non trouvée : " + invoiceId));

        // 2. Mettre à jour livraison
        facture.setDelivered(paiement);
        if (paiement) {
            facture.setDeliveredDate(LocalDateTime.now());
        }

        // 3. Mettre à jour paiement
        //    On ajoute la différence entre l'ancien amountPaid et le nouveau
        BigDecimal paye = newAmountPaid.add(facture.getAmountPaid());
        facture.setAmountPaid(paye);
        facture.updateBalance(); // total - amountPaid

        invoiceRepository.save(facture);

        // 4. Si paid=true et paye>0, on enregistre le paiement + Input
        if (livrer && paye.compareTo(BigDecimal.ZERO) > 0) {
            // 4.a Créer le Payment
            Payment payment = new Payment();
            payment.setInvoice(facture);
            payment.setAmount(paye);
            payment.setModePaiement(Payment.ModePaiement.valueOf(modePaiement.name().toUpperCase()));
            payment.setPaymentDate(paymentDate != null ? paymentDate : LocalDateTime.now());
            payment.setInvoice(facture);
            paymentRepository.save(payment);

            // 4.b Créer la ligne Input
            Input input = new Input();
            input.setTitres(String.valueOf(facture.getReference()));
            input.setMontants(newAmountPaid);
            input.setModePaiement(Input.ModePaiement.valueOf(modePaiement.name().toLowerCase()));
            input.setCreatedAts(LocalDateTime.now());
            input.setSaveBy(saveBy);
            inputRepository.save(input);
        }

        return facture;
    }
}