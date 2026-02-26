package com.itxiop.tech.supplier.sandbox.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierInternalStatusTest {

    @Test
    void from_goodRating_returnsActive() {
        assertEquals(SupplierInternalStatus.ACTIVE, SupplierInternalStatus.from(SustainabilityRating.A));
        assertEquals(SupplierInternalStatus.ACTIVE, SupplierInternalStatus.from(SustainabilityRating.B));
    }

    @Test
    void from_badRating_returnsOnProbation() {
        assertEquals(SupplierInternalStatus.ON_PROBATION, SupplierInternalStatus.from(SustainabilityRating.C));
        assertEquals(SupplierInternalStatus.ON_PROBATION, SupplierInternalStatus.from(SustainabilityRating.D));
        assertEquals(SupplierInternalStatus.ON_PROBATION, SupplierInternalStatus.from(SustainabilityRating.E));
    }

    @Test
    void from_stringRating_works() {
        assertEquals(SupplierInternalStatus.ACTIVE, SupplierInternalStatus.from("A"));
        assertEquals(SupplierInternalStatus.ON_PROBATION, SupplierInternalStatus.from("C"));
    }

    @Test
    void ban_fromOnProbation_returnsDisqualified() {
        assertEquals(SupplierInternalStatus.DISQUALIFIED, SupplierInternalStatus.ON_PROBATION.ban());
    }

    @Test
    void ban_fromActive_throwsException() {
        assertThrows(IllegalStateException.class, SupplierInternalStatus.ACTIVE::ban);
    }

    @Test
    void ban_fromDisqualified_throwsException() {
        assertThrows(IllegalStateException.class, SupplierInternalStatus.DISQUALIFIED::ban);
    }

    @Test
    void onRatingChange_activeToOnProbation() {
        SupplierInternalStatus result = SupplierInternalStatus.ACTIVE.onRatingChange(SustainabilityRating.C);
        assertEquals(SupplierInternalStatus.ON_PROBATION, result);
    }

    @Test
    void onRatingChange_onProbationToActive() {
        SupplierInternalStatus result = SupplierInternalStatus.ON_PROBATION.onRatingChange(SustainabilityRating.A);
        assertEquals(SupplierInternalStatus.ACTIVE, result);
    }

    @Test
    void onRatingChange_disqualified_throwsException() {
        assertThrows(IllegalStateException.class,
            () -> SupplierInternalStatus.DISQUALIFIED.onRatingChange(SustainabilityRating.A));
    }

    @Test
    void canBan_onlyOnProbation() {
        assertFalse(SupplierInternalStatus.ACTIVE.canBan());
        assertTrue(SupplierInternalStatus.ON_PROBATION.canBan());
        assertFalse(SupplierInternalStatus.DISQUALIFIED.canBan());
    }

    @Test
    void canUpdateRating_notDisqualified() {
        assertTrue(SupplierInternalStatus.ACTIVE.canUpdateRating());
        assertTrue(SupplierInternalStatus.ON_PROBATION.canUpdateRating());
        assertFalse(SupplierInternalStatus.DISQUALIFIED.canUpdateRating());
    }
}
