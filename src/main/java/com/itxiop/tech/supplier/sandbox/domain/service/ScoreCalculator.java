package com.itxiop.tech.supplier.sandbox.domain.service;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreCalculator {

    private static final double TURNOVER_MULTIPLIER = 0.1;
    private static final double BONUS_MULTIPLIER = 1.25;

    public Set<Long> bonusTurnovers(Collection<Supplier> suppliers) {
        return suppliers.stream()
            .map(Supplier::getAnnualTurnover)
            .distinct()
            .sorted()
            .limit(2)
            .collect(Collectors.toSet());
    }

    public double calculate(Supplier supplier, Set<Long> bonusTurnoversForCountry) {
        double ratingConstant = supplier.getSustainabilityRating().getConstant();
        double base = supplier.getAnnualTurnover() * TURNOVER_MULTIPLIER * ratingConstant;
        return bonusTurnoversForCountry.contains(supplier.getAnnualTurnover()) ? base * BONUS_MULTIPLIER : base;
    }
}
