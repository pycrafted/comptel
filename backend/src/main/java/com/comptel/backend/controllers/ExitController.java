package com.comptel.backend.controllers;

import com.comptel.backend.entity.Exit;
import com.comptel.backend.services.ExitService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exits")
public class ExitController {
    private final ExitService exitService;

    public ExitController(ExitService exitService) {
        this.exitService = exitService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllExits() {
        List<Exit> exits = exitService.getExitRepository().findAll();
        List<Map<String, Object>> response = exits.stream()
                .map(this::mapExitToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getExitById(@PathVariable Long id) {
        try {
            Exit exit = exitService.findById(id);
            return ResponseEntity.ok(mapExitToResponse(exit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<Map<String, Object>>> getExitsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Exit> exits = exitService.findByDateRange(start, end);
        List<Map<String, Object>> response = exits.stream()
                .map(this::mapExitToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<Map<String, Object>>> getExitsByType(
            @PathVariable Exit.TypeDepense type) {
        List<Exit> exits = exitService.findByType(type);
        List<Map<String, Object>> response = exits.stream()
                .map(this::mapExitToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createExit(@RequestBody Map<String, Object> request) {
        try {
            String titre = (String) request.get("titre");
            BigDecimal montant = new BigDecimal(request.get("montant").toString());
            Exit.TypeDepense typeDepense = Exit.TypeDepense.valueOf((String) request.get("typeDepense"));
            Long userId = Long.parseLong(request.get("userId").toString());

            Exit exit = exitService.createExit(titre, montant, typeDepense, userId);
            return ResponseEntity.ok(createSuccessResponse(exit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateExit(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            String titre = (String) request.get("titre");
            BigDecimal montant = new BigDecimal(request.get("montant").toString());
            Exit.TypeDepense typeDepense = Exit.TypeDepense.valueOf((String) request.get("typeDepense"));

            Exit exit = exitService.updateExit(id, titre, montant, typeDepense);
            return ResponseEntity.ok(createSuccessResponse(exit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteExit(@PathVariable Long id) {
        try {
            exitService.deleteExit(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sortie supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    private Map<String, Object> mapExitToResponse(Exit exit) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", exit.getId());
        response.put("titre", exit.getTitre());
        response.put("montant", exit.getMontant());
        response.put("typeDepense", exit.getTypeDepense());
        response.put("createdAt", exit.getCreatedAt());
        response.put("saveBy", exit.getSaveBy() != null ? exit.getSaveBy().getId() : null);
        return response;
    }

    private Map<String, Object> createSuccessResponse(Exit exit) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", mapExitToResponse(exit));
        return response;
    }

    private Map<String, Object> createErrorResponse(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", e.getMessage());
        return response;
    }
}