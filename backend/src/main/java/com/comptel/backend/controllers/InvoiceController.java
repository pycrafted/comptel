package com.comptel.backend.controllers;

import com.comptel.backend.entity.*;
import com.comptel.backend.repository.GlobalSettingsRepository;
import com.comptel.backend.repository.ServiceRepository;
import com.comptel.backend.repository.UserRepository;
import com.comptel.backend.services.InvoiceService;
import com.sun.security.auth.UserPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final GlobalSettingsRepository globalSettingsRepository;

    public InvoiceController(InvoiceService invoiceService, ServiceRepository serviceRepository,
                             UserRepository userRepository, GlobalSettingsRepository globalSettingsRepository) {
        this.invoiceService = invoiceService;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    /**
     * Récupère toutes les factures et les formate en JSON.
     *
     * @return ResponseEntity contenant une liste de factures formatées avec un code HTTP 200 (OK).
     * Logique :
     * Appelle invoiceService.getInvoiceRepository().findAll() pour obtenir la liste des factures.
     * Transforme chaque facture en un Map<String, Object> (via mapInvoiceToResponse) pour formater la réponse JSON.
     * Retourne la liste formatée avec un code HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getInvoiceRepository().findAll();
        List<Map<String, Object>> response = invoices.stream().map(this::mapInvoiceToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Supprime une facture par son ID.
     * @param id Identifiant de la facture à supprimer.
     * @return ResponseEntity contenant une réponse JSON indiquant le succès avec un code HTTP 200 (OK).
     * @throws RuntimeException Si la facture n'existe pas.
     * Logique:
     * Vérifie si la facture existe avec findById(id). Si non, lance une exception.
     * Supprime la facture avec delete(invoice).
     * Retourne une réponse JSON indiquant le succès.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée : " + id));
        invoiceService.getInvoiceRepository().delete(invoice);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "La facture a été supprimée avec succès.");
        return ResponseEntity.ok(response);
    }

    /**
     *
     * Fournit les données nécessaires pour créer une nouvelle facture (services disponibles, prochaine référence,
        paramètres globaux).
     * @return ResponseEntity contenant un Map avec les données formatées et un code HTTP 200 (OK).
     Logique:
     * Récupère tous les services (serviceRepository.findAll()).
     * Calcule la prochaine référence en appelant findMaxReference() (expliqué précédemment).
     * Récupère les paramètres globaux (GlobalSettings) pour savoir si certaines options (confirmation de livraison,
        paiement partiel, antidate) sont activées.
     * Retourne un Map avec ces informations.
     */
    @GetMapping("/add")
    public ResponseEntity<Map<String, Object>> getAddInvoiceData() {
        List<Service> services = serviceRepository.findAll();
        Integer lastReference = invoiceService.getInvoiceRepository().findMaxReference();
        int nextReference = (lastReference != null ? lastReference : 7999) + 1;
        GlobalSettings settings = globalSettingsRepository.findById(1L).orElse(new GlobalSettings());

        Map<String, Object> response = new HashMap<>();
        response.put("services", services);
        response.put("nextReference", nextReference);
        response.put("useDeliveryConfirmation", settings.isUseDeliveryConfirmation());
        response.put("usePartialPayment", settings.isUsePartialPayment());
        response.put("useAntidate", settings.isUseAntidate());
        return ResponseEntity.ok(response);
    }

    /**
     * Crée une nouvelle facture à partir des données envoyées dans le corps de la requête.
        @param request Corps de la requête contenant les données de la facture (client, services, paiement, etc.).
        @return ResponseEntity contenant une réponse JSON avec les détails de la facture créée (code HTTP 200)
        ou une erreur (code HTTP 400).
     * @throws RuntimeException Si l'utilisateur par défaut ou les données sont invalides.
     Logique :
     * Extrait les données du corps JSON (client, téléphone, services, paiements, etc.) avec des méthodes
       utilitaires (extractLongList, parseBigDecimal, etc.).
     * Appelle invoiceService.createInvoice pour créer la facture.
     * Retourne une réponse JSON avec les détails de la facture créée (via createSuccessResponse) ou une erreur
        (via createErrorResponse).
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInvoice(@RequestBody Map<String, Object> request) {
        try {
            // Extraction des données de la requête
            String customer = (String) request.get("customer");
            String telephone = (String) request.get("telephone");
            boolean delivered = (boolean) request.getOrDefault("delivered", false);
            LocalDateTime invoiceDateTime = parseDateTime((String) request.get("invoiceDateTime"));

            // Extraction des services
            List<Long> serviceIds = extractLongList(request, "serviceIds");
            List<Integer> quantities = extractIntegerList(request, "quantites");
            List<BigDecimal> prices = extractBigDecimalList(request, "prixs");

            // Extraction des paiements
            Payment.ModePaiement paymentMode = parsePaymentMode((String) request.get("mode_paiement"));
            BigDecimal amountPaid = parseBigDecimal((String) request.get("amountPaye"));
            LocalDateTime paymentDate = parseDateTime((String) request.get("paymentDate"));

            // Utilisateur temporaire (à remplacer plus tard)
            User saveBy = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Utilisateur par défaut non trouvé"));

            // Création de la facture
            Invoice invoice = invoiceService.createInvoice(customer, telephone, delivered, invoiceDateTime,
                                                            serviceIds, quantities, prices,
                                                            paymentMode, amountPaid, paymentDate, saveBy);

            return ResponseEntity.ok(createSuccessResponse(invoice));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> patchInvoce(@PathVariable Long id, @RequestBody Map<String, Object> body
    ) {
        try {
            BigDecimal newAmountPaid = parseBigDecimal((String) body.get("amountPaye"));
            LocalDateTime paymentDate = body.containsKey("paymentDate") ? LocalDateTime.parse(body.get("paymentDate").toString()) : LocalDateTime.now();
            Payment.ModePaiement modePaiement = parsePaymentMode((String) body.get("mode_paiement"));
            boolean livrer = Boolean.parseBoolean(body.get("livrer").toString());
            boolean paiement      = Boolean.parseBoolean(body.get("paiement").toString());

            // On récupère l'utilisateur courant
            User saveBy = userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            Invoice updated = invoiceService.updateInvoicePayment(
                    id, newAmountPaid, paymentDate, modePaiement, livrer, paiement, saveBy);

            return ResponseEntity.ok(createSuccessResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<Map<String, Object>>> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<Invoice> invoices = invoiceService.getInvoiceRepository().findByInvoiceDateTimeBetween(start, end);
            List<Map<String, Object>> response = invoices.stream()
                    .map(this::mapInvoiceToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(List.of(Map.of("error", "Erreur lors de la récupération des factures: " + e.getMessage())));
        }
    }

    @GetMapping("/totals/by-date-range")
    public ResponseEntity<Map<String, Object>> getTotalsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<Invoice> invoices = invoiceService.getInvoiceRepository().findByInvoiceDateTimeBetween(start, end);
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalPaid = BigDecimal.ZERO;
            BigDecimal totalBalance = BigDecimal.ZERO;
            
            for (Invoice invoice : invoices) {
                totalAmount = totalAmount.add(invoice.getTotal());
                totalPaid = totalPaid.add(invoice.getAmountPaid());
                totalBalance = totalBalance.add(invoice.getBalance());
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalAmount", totalAmount.toString());
            response.put("totalPaid", totalPaid.toString());
            response.put("totalBalance", totalBalance.toString());
            response.put("invoiceCount", invoices.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erreur lors du calcul des totaux: " + e.getMessage()));
        }
    }

    /**
     * Récupère les factures pour une date spécifique (journal).
     *
     * @param date Date au format YYYY-MM-DD
     * @return ResponseEntity contenant les factures du jour avec un code HTTP 200 (OK).
     */
    @GetMapping("/journal")
    public ResponseEntity<List<Map<String, Object>>> getJournalByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            // Créer les dates de début et fin pour la journée
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999);
            
            List<Invoice> invoices = invoiceService.getInvoiceRepository().findByInvoiceDateTimeBetween(startOfDay, endOfDay);
            List<Map<String, Object>> response = invoices.stream()
                    .map(this::mapInvoiceToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(List.of(Map.of("error", "Erreur lors de la récupération du journal: " + e.getMessage())));
        }
    }

    // Méthodes utilitaires privées

    /**
     * Formate une facture pour la réponse JSON, incluant les services et la date du dernier paiement.
     * @param invoice
     * @return Formate une facture pour la réponse JSON (inclut les services et le dernier paiement).
     */
    private Map<String, Object> mapInvoiceToResponse(Invoice invoice) {
        Map<String, Object> invoiceData = new HashMap<>();
        invoiceData.put("id", invoice.getId());
        invoiceData.put("reference", invoice.getReference());
        invoiceData.put("customer", invoice.getCustomer());
        invoiceData.put("total", invoice.getTotal().toString());
        invoiceData.put("balance", invoice.getBalance().toString());
        invoiceData.put("delivered", invoice.isDelivered() ? "Oui" : "Non");
        invoiceData.put("invoiceDateTime", invoice.getInvoiceDateTime().toString());

        String paidStatus = invoice.isFullyPaid() ? "Oui" :
                (invoice.getBalance().equals(invoice.getTotal()) ? "Non" : "En cours");
        invoiceData.put("paid", paidStatus);

        // Ne pas essayer d'accéder aux services et paiements pour éviter les erreurs de lazy loading
        invoiceData.put("invoiceServices", new java.util.ArrayList<>());
        invoiceData.put("lastPaymentDate", null);

        return invoiceData;
    }

    /**
     * Formate les lignes de facture pour la réponse JSON.
     * @param invoice
     * @return Formate les lignes de facture
     */
    private java.util.List<java.util.Map<String, Object>> mapServicesToResponse(Invoice invoice) {
        return invoice.getInvoiceServices().stream().map(service -> {
            java.util.Map<String, Object> serviceData = new java.util.HashMap<>();
            serviceData.put("serviceName", service.getService().getDesignation());
            serviceData.put("quantite", service.getQuantite());
            serviceData.put("prix", service.getPrix().toString());
            return serviceData;
        }).collect(java.util.stream.Collectors.toList());
    }

    /**
     *  Récupère la date du dernier paiement d'une facture.
     * @param invoice
     * @return Récupère la date du dernier paiement.
     */
    private String getLastPaymentDate(Invoice invoice) {
        try {
            return invoice.getPayments().stream()
                    .max((p1, p2) -> p1.getPaymentDate().compareTo(p2.getPaymentDate()))
                    .map(p -> p.getPaymentDate().toString())
                    .orElse(null);
        } catch (Exception e) {
            // Si les paiements ne sont pas chargés, retourner null
            return null;
        }
    }

    /**
     * Crée une réponse JSON standardisée pour une facture créée avec succès.
     * @param invoice
     * @return Crée une réponse JSON standardisée.
     */
    private Map<String, Object> createSuccessResponse(Invoice invoice) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("total", invoice.getTotal().toString());
        response.put("balance", invoice.getBalance().toString());
        response.put("amount_paid", invoice.getAmountPaid().toString());
        response.put("customer", invoice.getCustomer());
        response.put("id", invoice.getId());
        response.put("reference", invoice.getReference());

        String paidStatus = invoice.isFullyPaid() ? "Oui" :
                (invoice.getBalance().equals(invoice.getTotal()) ? "Non" : "En cours");
        response.put("paid", paidStatus);

        response.put("delivered", invoice.isDelivered() ? "Oui" : "Non");
        response.put("invoiceDateTime", invoice.getInvoiceDateTime().toString());
        
        // Ne pas essayer d'accéder aux services pour éviter les erreurs de lazy loading
        response.put("invoiceServices", new java.util.ArrayList<>());

        return response;
    }

    /**
     * Crée une réponse JSON standardisée pour une erreur.
     * @param e
     * @return Crée des réponse JSON standardisée.
     */
    private Map<String, Object> createErrorResponse(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", e.getMessage());
        return errorResponse;
    }

    // Méthodes d'extraction et de parsing
    // Convertissent les données JSON en types Java.

    /**
     * Extrait une liste de Long à partir d'une clé dans la requête JSON.
     *
     * @param request Données JSON de la requête.
     * @param key Clé contenant la liste d'entiers.
     * @return Liste de Long extraite.
     */
    private List<Long> extractLongList(Map<String, Object> request, String key) {
        @SuppressWarnings("unchecked")
        List<Integer> list = (List<Integer>) request.get(key);
        return list.stream().map(Integer::longValue).collect(Collectors.toList());
    }

    /**
     * Extrait une liste d'entiers à partir d'une clé dans la requête JSON.
     *
     * @param request Données JSON de la requête.
     * @param key Clé contenant la liste d'entiers.
     * @return Liste d'entiers extraite.
     */
    private List<Integer> extractIntegerList(Map<String, Object> request, String key) {
        @SuppressWarnings("unchecked")
        List<Integer> list = (List<Integer>) request.get(key);
        return list;
    }

    /**
     * Extrait une liste de BigDecimal à partir d'une clé dans la requête JSON.
     *
     * @param request Données JSON de la requête.
     * @param key Clé contenant la liste de chaînes représentant des nombres.
     * @return Liste de BigDecimal extraite.
     */
    private List<BigDecimal> extractBigDecimalList(Map<String, Object> request, String key) {
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) request.get(key);
        return list.stream().map(BigDecimal::new).collect(Collectors.toList());
    }

    /**
     * Convertit une chaîne en LocalDateTime.
     *
     * @param dateTimeStr Chaîne représentant une date/heure.
     * @return LocalDateTime parsed ou null si la chaîne est null.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        return dateTimeStr != null ? LocalDateTime.parse(dateTimeStr) : null;
    }

    /**
     * Convertit une chaîne en mode de paiement.
     *
     * @param modeStr Chaîne représentant le mode de paiement.
     * @return ModePaiement parsed ou null si la chaîne est null.
     */
    private Payment.ModePaiement parsePaymentMode(String modeStr) {
        return modeStr != null ? Payment.ModePaiement.valueOf(modeStr.toUpperCase()) : null;
    }

    /**
     * Convertit une chaîne en BigDecimal.
     *
     * @param valueStr Chaîne représentant un nombre.
     * @return BigDecimal parsed ou null si la chaîne est null.
     */
    private BigDecimal parseBigDecimal(String valueStr) {
        return valueStr != null ? new BigDecimal(valueStr) : null;
    }
}