package com.itxiop.tech.supplier.sandbox.domain.service;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScoreCalculator {
    private static final Map<String, Double> RATING_CONSTANTS = Map.of(
        "A", 1.0, "B", 0.75, "C", 0.5, "D", 0.25, "E", 0.1
    );

    public Set<Long> bonusTurnovers(Collection<Supplier> suppliers) {
        return suppliers.stream()
            .map(Supplier::getAnnualTurnover)
            .distinct()
            .sorted()
            .limit(2)
            .collect(Collectors.toSet());
    }

    public double calculate(Supplier supplier, Set<Long> bonusTurnoversForCountry) {
        Double ratingConstant = RATING_CONSTANTS.get(supplier.getSustainabilityRating());
        if (ratingConstant == null) throw new IllegalArgumentException("Unknown rating: " + supplier.getSustainabilityRating());
        double base = supplier.getAnnualTurnover() * 0.1 * ratingConstant;
        return bonusTurnoversForCountry.contains(supplier.getAnnualTurnover()) ? base * 1.25 : base;
    }
}
