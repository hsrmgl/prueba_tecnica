package com.itxiop.tech.supplier.sandbox.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class SustainabilityRatingTest {

    @Test
    void fromString_validRatings() {
        assertEquals(SustainabilityRating.A, SustainabilityRating.fromString("A"));
        assertEquals(SustainabilityRating.B, SustainabilityRating.fromString("B"));
        assertEquals(SustainabilityRating.C, SustainabilityRating.fromString("C"));
        assertEquals(SustainabilityRating.D, SustainabilityRating.fromString("D"));
        assertEquals(SustainabilityRating.E, SustainabilityRating.fromString("E"));
    }

    @Test
    void fromString_invalidRating_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> SustainabilityRating.fromString("X"));
        assertThrows(IllegalArgumentException.class, () -> SustainabilityRating.fromString(""));
    }

    @Test
    void isGoodRating_AandB_areGood() {
        assertTrue(SustainabilityRating.A.isGoodRating());
        assertTrue(SustainabilityRating.B.isGoodRating());
    }

    @ParameterizedTest
    @EnumSource(value = SustainabilityRating.class, names = {"C", "D", "E"})
    void isGoodRating_CDE_areNotGood(SustainabilityRating rating) {
        assertFalse(rating.isGoodRating());
    }

    @Test
    void constants_areCorrect() {
        assertEquals(1.0, SustainabilityRating.A.getConstant());
        assertEquals(0.75, SustainabilityRating.B.getConstant());
        assertEquals(0.5, SustainabilityRating.C.getConstant());
        assertEquals(0.25, SustainabilityRating.D.getConstant());
        assertEquals(0.1, SustainabilityRating.E.getConstant());
    }
}
