package com.comptel.backend.controllers;

import com.comptel.backend.entity.Invoice;
import com.comptel.backend.entity.Input;
import com.comptel.backend.entity.Exit;
import com.comptel.backend.repository.InvoiceRepository;
import com.comptel.backend.repository.InputRepository;
import com.comptel.backend.repository.ExitRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final InvoiceRepository invoiceRepository;
    private final InputRepository inputRepository;
    private final ExitRepository exitRepository;

    public DashboardController(InvoiceRepository invoiceRepository, 
                             InputRepository inputRepository, 
                             ExitRepository exitRepository) {
        this.invoiceRepository = invoiceRepository;
        this.inputRepository = inputRepository;
        this.exitRepository = exitRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            // Date d'aujourd'hui
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            // Date du mois en cours
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
            LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
            LocalDateTime endOfMonthDateTime = endOfMonth.atTime(LocalTime.MAX);

            // Statistiques globales (toutes les factures)
            List<Invoice> allInvoices = invoiceRepository.findAll();
            BigDecimal totalInvoicesAmount = allInvoices.stream()
                    .map(Invoice::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalInvoicesPaid = allInvoices.stream()
                    .map(Invoice::getAmountPaid)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalInvoicesBalance = allInvoices.stream()
                    .map(Invoice::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Statistiques du jour
            List<Invoice> todayInvoices = invoiceRepository.findByInvoiceDateTimeBetween(startOfDay, endOfDay);
            BigDecimal todayInvoicesAmount = todayInvoices.stream()
                    .map(Invoice::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal todayInvoicesPaid = todayInvoices.stream()
                    .map(Invoice::getAmountPaid)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Statistiques du mois
            List<Invoice> monthInvoices = invoiceRepository.findByInvoiceDateTimeBetween(startOfMonthDateTime, endOfMonthDateTime);
            BigDecimal monthInvoicesAmount = monthInvoices.stream()
                    .map(Invoice::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal monthInvoicesPaid = monthInvoices.stream()
                    .map(Invoice::getAmountPaid)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Entrées du jour
            List<Input> todayInputs = inputRepository.findByCreatedAtsBetween(startOfDay, endOfDay);
            BigDecimal todayInputsAmount = todayInputs.stream()
                    .map(Input::getMontants)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Entrées du mois
            List<Input> monthInputs = inputRepository.findByCreatedAtsBetween(startOfMonthDateTime, endOfMonthDateTime);
            BigDecimal monthInputsAmount = monthInputs.stream()
                    .map(Input::getMontants)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sorties du jour
            List<Exit> todayExits = exitRepository.findByCreatedAtBetween(startOfDay, endOfDay);
            BigDecimal todayExitsAmount = todayExits.stream()
                    .map(Exit::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sorties du mois
            List<Exit> monthExits = exitRepository.findByCreatedAtBetween(startOfMonthDateTime, endOfMonthDateTime);
            BigDecimal monthExitsAmount = monthExits.stream()
                    .map(Exit::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Factures récentes (5 dernières)
            List<Invoice> recentInvoices = invoiceRepository.findTop5ByOrderByInvoiceDateTimeDesc();

            Map<String, Object> response = new HashMap<>();
            
            // Statistiques globales
            response.put("totalInvoices", allInvoices.size());
            response.put("totalInvoicesAmount", totalInvoicesAmount.toString());
            response.put("totalInvoicesPaid", totalInvoicesPaid.toString());
            response.put("totalInvoicesBalance", totalInvoicesBalance.toString());

            // Statistiques du jour
            response.put("todayInvoices", todayInvoices.size());
            response.put("todayInvoicesAmount", todayInvoicesAmount.toString());
            response.put("todayInvoicesPaid", todayInvoicesPaid.toString());
            response.put("todayInputsAmount", todayInputsAmount.toString());
            response.put("todayExitsAmount", todayExitsAmount.toString());

            // Statistiques du mois
            response.put("monthInvoices", monthInvoices.size());
            response.put("monthInvoicesAmount", monthInvoicesAmount.toString());
            response.put("monthInvoicesPaid", monthInvoicesPaid.toString());
            response.put("monthInputsAmount", monthInputsAmount.toString());
            response.put("monthExitsAmount", monthExitsAmount.toString());

            // Factures récentes
            List<Map<String, Object>> recentInvoicesData = recentInvoices.stream()
                    .map(this::mapInvoiceToDashboardResponse)
                    .collect(Collectors.toList());
            response.put("recentInvoices", recentInvoicesData);

            // Statistiques par statut
            long paidInvoices = allInvoices.stream().filter(Invoice::isFullyPaid).count();
            long unpaidInvoices = allInvoices.stream().filter(i -> i.getBalance().equals(i.getTotal())).count();
            long partialInvoices = allInvoices.size() - paidInvoices - unpaidInvoices;

            response.put("paidInvoices", paidInvoices);
            response.put("unpaidInvoices", unpaidInvoices);
            response.put("partialInvoices", partialInvoices);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de la récupération des statistiques: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/stats/period")
    public ResponseEntity<Map<String, Object>> getStatsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            // Factures de la période
            List<Invoice> periodInvoices = invoiceRepository.findByInvoiceDateTimeBetween(startDateTime, endDateTime);
            BigDecimal periodInvoicesAmount = periodInvoices.stream()
                    .map(Invoice::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal periodInvoicesPaid = periodInvoices.stream()
                    .map(Invoice::getAmountPaid)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Entrées de la période
            List<Input> periodInputs = inputRepository.findByCreatedAtsBetween(startDateTime, endDateTime);
            BigDecimal periodInputsAmount = periodInputs.stream()
                    .map(Input::getMontants)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sorties de la période
            List<Exit> periodExits = exitRepository.findByCreatedAtBetween(startDateTime, endDateTime);
            BigDecimal periodExitsAmount = periodExits.stream()
                    .map(Exit::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> response = new HashMap<>();
            response.put("periodInvoices", periodInvoices.size());
            response.put("periodInvoicesAmount", periodInvoicesAmount.toString());
            response.put("periodInvoicesPaid", periodInvoicesPaid.toString());
            response.put("periodInputsAmount", periodInputsAmount.toString());
            response.put("periodExitsAmount", periodExitsAmount.toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de la récupération des statistiques: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/activity")
    public ResponseEntity<List<Map<String, Object>>> getActivityData() {
        try {
            List<Map<String, Object>> activityData = new ArrayList<>();
            LocalDate today = LocalDate.now();

            // Récupérer les données des 7 derniers jours
            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

                // Factures du jour
                List<Invoice> dayInvoices = invoiceRepository.findByInvoiceDateTimeBetween(startOfDay, endOfDay);
                BigDecimal dayInvoicesAmount = dayInvoices.stream()
                        .map(Invoice::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Entrées du jour
                List<Input> dayInputs = inputRepository.findByCreatedAtsBetween(startOfDay, endOfDay);
                BigDecimal dayInputsAmount = dayInputs.stream()
                        .map(Input::getMontants)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Sorties du jour
                List<Exit> dayExits = exitRepository.findByCreatedAtBetween(startOfDay, endOfDay);
                BigDecimal dayExitsAmount = dayExits.stream()
                        .map(Exit::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date.toString());
                dayData.put("dayName", date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.FRENCH));
                dayData.put("invoices", dayInvoices.size());
                dayData.put("invoicesAmount", dayInvoicesAmount.toString());
                dayData.put("inputsAmount", dayInputsAmount.toString());
                dayData.put("exitsAmount", dayExitsAmount.toString());

                activityData.add(dayData);
            }

            return ResponseEntity.ok(activityData);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de la récupération des données d'activité: " + e.getMessage());
            return ResponseEntity.badRequest().body(List.of(errorResponse));
        }
    }

    private Map<String, Object> mapInvoiceToDashboardResponse(Invoice invoice) {
        Map<String, Object> invoiceData = new HashMap<>();
        invoiceData.put("id", invoice.getId());
        invoiceData.put("reference", invoice.getReference());
        invoiceData.put("customer", invoice.getCustomer());
        invoiceData.put("total", invoice.getTotal().toString());
        invoiceData.put("balance", invoice.getBalance().toString());
        invoiceData.put("amountPaid", invoice.getAmountPaid().toString());
        invoiceData.put("invoiceDateTime", invoice.getInvoiceDateTime().toString());
        invoiceData.put("delivered", invoice.isDelivered());

        // Statut de paiement
        String status;
        if (invoice.isFullyPaid()) {
            status = "payé";
        } else if (invoice.getBalance().equals(invoice.getTotal())) {
            status = "non payé";
        } else {
            status = "en cours";
        }
        invoiceData.put("status", status);

        return invoiceData;
    }
} 