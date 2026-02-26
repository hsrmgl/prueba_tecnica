package com.itxiop.tech.supplier.sandbox.domain.service;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.model.SustainabilityRating;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculatorTest {

    private final ScoreCalculator calculator = new ScoreCalculator();

    private Supplier supplier(int duns, String country, long turnover, SustainabilityRating rating) {
        return Supplier.create(duns, "Supplier-" + duns, country, turnover, rating);
    }

    @Test
    void bonusTurnovers_returnsTwoLowestUniqueTurnovers() {
        List<Supplier> suppliers = List.of(
            supplier(1, "ES", 200, SustainabilityRating.A),
            supplier(2, "ES", 200, SustainabilityRating.A),
            supplier(3, "ES", 200, SustainabilityRating.A),
            supplier(4, "ES", 210, SustainabilityRating.A),
            supplier(5, "ES", 250, SustainabilityRating.A)
        );
        Set<Long> bonus = calculator.bonusTurnovers(suppliers);
        assertEquals(Set.of(200L, 210L), bonus);
    }

    @Test
    void bonusTurnovers_singleSupplier_returnsOneTurnover() {
        List<Supplier> suppliers = List.of(
            supplier(1, "FR", 1500000, SustainabilityRating.B)
        );
        Set<Long> bonus = calculator.bonusTurnovers(suppliers);
        assertEquals(Set.of(1500000L), bonus);
    }

    @Test
    void calculate_withoutBonus() {
        Supplier s = supplier(1, "ES", 4500000, SustainabilityRating.D);
        double score = calculator.calculate(s, Set.of(2200000L, 3000000L));
        assertEquals(112500.0, score, 0.01);
    }

    @Test
    void calculate_withBonus() {
        Supplier s = supplier(1, "ES", 2200000, SustainabilityRating.B);
        double score = calculator.calculate(s, Set.of(2200000L, 3000000L));
        assertEquals(206250.0, score, 0.01);
    }

    @Test
    void calculate_ratingA_fullConstant() {
        Supplier s = supplier(1, "DE", 2000000, SustainabilityRating.A);
        double score = calculator.calculate(s, Set.of(2000000L, 2100000L));
        assertEquals(250000.0, score, 0.01);
    }

    @Test
    void calculate_ratingE_lowestConstant() {
        Supplier s = supplier(1, "DE", 16500000, SustainabilityRating.E);
        double score = calculator.calculate(s, Set.of(2000000L, 2100000L));
        assertEquals(165000.0, score, 0.01);
    }
}
