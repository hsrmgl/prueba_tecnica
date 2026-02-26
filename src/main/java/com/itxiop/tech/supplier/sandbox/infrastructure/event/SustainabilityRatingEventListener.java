package com.itxiop.tech.supplier.sandbox.infrastructure.event;

import com.itxiop.tech.supplier.sandbox.domain.model.SustainabilityRating;
import com.itxiop.tech.supplier.sustainability.SustainabilityRatingEvent;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SustainabilityRatingEventListener {

    private final SupplierRepositoryPort supplierRepo;

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
