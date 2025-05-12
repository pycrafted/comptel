package com.comptel.backend.controllers;


import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;
import com.comptel.backend.services.ServiceF;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final ServiceF ServiceEntityServ;

    public ServiceController(ServiceRepository serviceRepository, ServiceF serviceF) {
        this.serviceRepository = serviceRepository;
        this.ServiceEntityServ = serviceF;
    }

    /**
     * Récupère toutes les services et les formatent en JSON.
     *
     * @return ResponseEntity contenant une liste de factures formatées avec un code HTTP 200 (OK).
     * Logique :
     * Appelle ServiceEntityServ.getServiceRepository().findAll() pour obtenir la liste des services.
     * Transforme chaque service en un Map<String, Object> (via mapInvoiceToResponse) pour formater la réponse JSON.
     * Retourne la liste formatée avec un code HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllService() {
        List<Service> services = ServiceEntityServ.getServiceRepository().findAll();
        List<Map<String, Object>> response = services.stream().map(this::mapServiceToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getServiceById(@PathVariable Long id){
        try {
            Service serviceEntity = ServiceEntityServ.findById(id);
            Map<String, Object> response = mapServiceToResponse(serviceEntity);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    /**
     * Crée un nouveau Service à partir des données envoyées dans le corps de la requête.
     @param request Corps de la requête contenant les données du service (designation, prix, proposition).
     @return ResponseEntity contenant une réponse JSON avec les détails de la facture créée (code HTTP 200)
     ou une erreur (code HTTP 400).
      * @throws RuntimeException Si les données sont invalides.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> CreatService(@RequestBody Map<String, Object> request){
        try {
            // Extraction des données de la requête
            String designation = (String) request.get("designation");
//            BigDecimal prix = (BigDecimal) request.get("prix");
            Object prixObj = request.get("prix");
            BigDecimal prix = prixObj instanceof Number ? new BigDecimal(prixObj.toString()) : (BigDecimal) prixObj;
            String proposition = (String) request.get("proposition");

            // Création de services
            Service Service = ServiceEntityServ.CreateServiceEntity(
                    designation, prix, proposition);

            return ResponseEntity.ok(createSuccessResponse(Service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    /**
     *
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> UpdateService(@PathVariable Long id,@RequestBody Map<String, Object> request){
        try {
            // Extraction des données de la requête
            String designation = (String) request.get("designation");
            BigDecimal prix = (BigDecimal) request.get("prix");
            String proposition = (String) request.get("proposition");

            // Valider la modification
            Service Service = ServiceEntityServ.UpdateService(id,designation, prix, proposition);

            return ResponseEntity.ok(createSuccessResponse(Service));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e));
        }
    }

    /**
     * Supprime un service par son ID.
     * @param id Identifiant du service à supprimer.
     * @return ResponseEntity contenant une réponse JSON indiquant le succès avec un code HTTP 200 (OK).
     * @throws RuntimeException Si la facture n'existe pas.
     * Logique:
     * Vérifie si le service existe avec findById(id). Si non, lance une exception.
     * Supprime le service avec delete(service).
     * Retourne une réponse JSON indiquant le succès.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteService(@PathVariable Long id) {
        Service service = ServiceEntityServ.getServiceRepository().findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvée : " + id));
        ServiceEntityServ.getServiceRepository().delete(service);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Le Service a été supprimé avec succès.");
        return ResponseEntity.ok(response);
    }

    /**
     * Crée une réponse JSON standardisée pour une facture créée avec succès.
     * @param service
     * @return Crée une réponse JSON standardisée.
     */
    private Map<String, Object> createSuccessResponse(Service service) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("proposition", service.getProposition());
        response.put("designation", service.getDesignation());
        response.put("prix", service.getPrix());

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

    /**
     * Formate une service pour la réponse JSON.
     * @param service
     * @return Formate une facture pour la réponse JSON (inclut les services et le dernier paiement).
     */
    private Map<String, Object> mapServiceToResponse(Service service) {
        Map<String, Object> ServiceData = new HashMap<>();
        ServiceData.put("id", service.getId());
        ServiceData.put("designation", service.getDesignation());
        ServiceData.put("proposition", service.getProposition());
        ServiceData.put("prix", service.getPrix());

        return ServiceData;
    }
}
