package com.itxiop.tech.supplier.sustainability;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SustainabilityRatingEvent extends ApplicationEvent {

    private final int duns;

    private final String score;

    public SustainabilityRatingEvent(Object source, int duns, String score) {
        super(source);
        this.duns = duns;
        this.score = score;
    }

}
