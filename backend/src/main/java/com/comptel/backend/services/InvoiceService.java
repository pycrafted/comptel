package com.comptel.backend.services;

import com.comptel.backend.entity.*;
import com.comptel.backend.repository.InvoiceLineRepository;
import com.comptel.backend.repository.InvoiceRepository;
import com.comptel.backend.repository.PaymentRepository;
import com.comptel.backend.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ServiceRepository serviceRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final PaymentRepository paymentRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, ServiceRepository serviceRepository,
                          InvoiceLineRepository invoiceLineRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.serviceRepository = serviceRepository;
        this.invoiceLineRepository = invoiceLineRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Retourne le repository des factures pour permettre des opérations supplémentaires.
     *
     * @return InvoiceRepository pour accéder aux opérations sur les factures.
     */
    public InvoiceRepository getInvoiceRepository() {
        return invoiceRepository;
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
        invoice.setAmountPaid(BigDecimal.ZERO);
        invoice.updateBalance();

        invoiceRepository.save(invoice);
        invoiceLineRepository.saveAll(invoiceLines);

        if (modePaiement != null && amountPaid != null && amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            Payment payment = new Payment();
            payment.setInvoice(invoice);
            payment.setAmount(amountPaid);
            payment.setModePaiement(modePaiement);
            payment.setPaymentDate(paymentDate != null ? paymentDate : LocalDateTime.now());
            paymentRepository.save(payment);
            invoice.setAmountPaid(amountPaid);
            invoice.updateBalance();
            invoiceRepository.save(invoice);
        }

        return invoice;
    }
}