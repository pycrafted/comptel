package com.comptel.backend.services;

import com.comptel.backend.entity.Service;
import com.comptel.backend.repository.ServiceRepository;

import java.math.BigDecimal;

@org.springframework.stereotype.Service
public class ServiceF {
    private final ServiceRepository serviceRepository;

    public ServiceF(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     *
     * @param designation
     * @param prix
     * @param proposition
     * @return
     */
    public Service CreateServiceEntity(String designation, BigDecimal prix, String proposition){
        Service service = new Service();
        service.setDesignation(designation);
        service.setPrix(prix);
        service.setProposition(proposition);

        serviceRepository.save(service);
        return service;
    }

    /**
     *
     * @param id
     * @param designation
     * @param prix
     * @param proposition
     * @return
     */
    public Service UpdateService(Long id, String designation, BigDecimal prix, String proposition){
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé : " + id));
        service.setDesignation(designation);
        service.setPrix(prix);
        service.setProposition(proposition);

        serviceRepository.save(service);
        return service;
    }

    /**
     * Retourne le repository des factures pour permettre des opérations supplémentaires.
     *
     * @return InvoiceRepository pour accéder aux opérations sur les factures.
     */
    public ServiceRepository getServiceRepository() {
        return serviceRepository;
    }

    /**
     *
     * @param id
     * @return
     */
    public Service findById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé : " + id));
    }
}
