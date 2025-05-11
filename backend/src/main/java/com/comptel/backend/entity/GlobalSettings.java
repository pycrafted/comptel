package com.comptel.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
//@Data
public class GlobalSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean useDeliveryConfirmation = true;
    private boolean usePartialPayment = true;
    private boolean useAntidate = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUseDeliveryConfirmation() {
        return useDeliveryConfirmation;
    }

    public void setUseDeliveryConfirmation(boolean useDeliveryConfirmation) {
        this.useDeliveryConfirmation = useDeliveryConfirmation;
    }

    public boolean isUsePartialPayment() {
        return usePartialPayment;
    }

    public void setUsePartialPayment(boolean usePartialPayment) {
        this.usePartialPayment = usePartialPayment;
    }

    public boolean isUseAntidate() {
        return useAntidate;
    }

    public void setUseAntidate(boolean useAntidate) {
        this.useAntidate = useAntidate;
    }
}