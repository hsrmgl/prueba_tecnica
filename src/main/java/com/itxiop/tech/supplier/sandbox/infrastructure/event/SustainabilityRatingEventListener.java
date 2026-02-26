package com.itxiop.tech.supplier.sandbox.infrastructure.event;

import com.itxiop.tech.supplier.sandbox.domain.model.SustainabilityRating;
import com.itxiop.tech.supplier.sustainability.SustainabilityRatingEvent;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SustainabilityRatingEventListener {

    private final SupplierRepositoryPort supplierRepo;

    public SustainabilityRatingEventListener(SupplierRepositoryPort supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    @EventListener
    public void onSustainabilityRatingEvent(SustainabilityRatingEvent event) {
        SustainabilityRating newRating = SustainabilityRating.fromString(event.getScore());
        supplierRepo.findByDuns(event.getDuns()).ifPresent(supplier -> {
            if (supplier.getInternalStatus().canUpdateRating()) {
                supplierRepo.save(supplier.withRating(newRating));
            }
        });
    }
}
