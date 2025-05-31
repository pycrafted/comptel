package com.comptel.backend.controllers;

import com.comptel.backend.entity.Input;
import com.comptel.backend.services.InputService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inputs")
public class InputController {

    private final InputService inputService;

    public InputController(InputService inputService) {
        this.inputService = inputService;
    }
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllInputs() {
        List<Input> inputs = inputService.getInputRepository().findAll();
        List<Map<String, Object>> response = inputs.stream().map(this::mapInputToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getInputById(@PathVariable Long id) {
        try {
            Input input = inputService.findById(id);
            return ResponseEntity.ok(mapInputToResponse(input));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Map<String, Object>>> getInputsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Input> inputs = inputService.findByDateRange(start, end);
        List<Map<String, Object>> response = inputs.stream()
                .map(this::mapInputToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-mode/{mode}")
    public ResponseEntity<List<Map<String, Object>>> getInputsByMode(
            @PathVariable Input.ModePaiement mode) {
        List<Input> inputs = inputService.findByModePaiement(mode);
        List<Map<String, Object>> response = inputs.stream()
                .map(this::mapInputToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /***
     * A revoir
     * @param userId
     * @return
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getInputsByUser(@PathVariable Long userId) {
        List<Input> inputs = inputService.findByUserId(userId);
        List<Map<String, Object>> response = inputs.stream()
                .map(this::mapInputToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createInput(@RequestBody Map<String, Object> request) {
        try {
            String titres = (String) request.get("titres");
            BigDecimal montants = new BigDecimal(request.get("montants").toString());
            String modePaiement = (String) request.get("modePaiement");
            Long userId = Long.parseLong(request.get("saveBy").toString());

            Input input = inputService.createInput(titres, montants, modePaiement, userId);
            return ResponseEntity.ok(createSuccessResponse(input));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInput(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String titres = (String) request.get("titres");
            BigDecimal montants = new BigDecimal(request.get("montants").toString());
            String modePaiement = (String) request.get("modePaiement");

            Input input = inputService.updateInput(id, titres, montants, modePaiement);
            return ResponseEntity.ok(createSuccessResponse(input));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInput(@PathVariable Long id) {
        try {
            inputService.deleteInput(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Input supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    private Map<String, Object> mapInputToResponse(Input input) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", input.getId());
        map.put("titres", input.getTitres());
        map.put("montants", input.getMontants());
        map.put("modePaiement", input.getModePaiement().toString());
        map.put("createdAts", input.getCreatedAts());
        map.put("saveBy", input.getSaveBy() != null ? input.getSaveBy().getId() : null);
        return map;
    }

    private Map<String, Object> createSuccessResponse(Input input) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", mapInputToResponse(input));
        return response;
    }

    private Map<String, Object> createErrorResponse(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", e.getMessage());
        return error;
    }

}
