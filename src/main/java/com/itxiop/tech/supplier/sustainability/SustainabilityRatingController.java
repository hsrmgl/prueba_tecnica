package com.itxiop.tech.supplier.sustainability;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class SustainabilityRatingController {

    private final ApplicationEventPublisher publisher;

    @PostMapping("/sustainability/update")
    void updateSustainability(@RequestBody SustainabilityRating score) {
        SustainabilityRatingEvent event = new SustainabilityRatingEvent(this, score.getDuns(), score.getScore());
        publisher.publishEvent(event);
    }

}
