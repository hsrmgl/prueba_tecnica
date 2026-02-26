package com.itxiop.tech.supplier.sandbox.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @Test
    void create_withGoodRating_isActive() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
        assertEquals(SupplierInternalStatus.ACTIVE, supplier.getInternalStatus());
        assertFalse(supplier.isDisqualified());
        assertFalse(supplier.isOnProbation());
        assertEquals("Active", supplier.getApiStatus());
    }

    @Test
    void create_withBadRating_isOnProbation() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.D);
        assertEquals(SupplierInternalStatus.ON_PROBATION, supplier.getInternalStatus());
        assertFalse(supplier.isDisqualified());
        assertTrue(supplier.isOnProbation());
        assertEquals("Active", supplier.getApiStatus());
    }

    @Test
    void ban_fromOnProbation_becomesDisqualified() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.C);
        Supplier banned = supplier.ban();
        assertTrue(banned.isDisqualified());
        assertEquals("Disqualified", banned.getApiStatus());
        assertEquals(supplier.getDuns(), banned.getDuns());
        assertEquals(supplier.getName(), banned.getName());
    }

    @Test
    void ban_fromActive_throwsException() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
        assertThrows(IllegalStateException.class, supplier::ban);
    }

    @Test
    void withRating_promotesFromOnProbationToActive() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.C);
        assertEquals(SupplierInternalStatus.ON_PROBATION, supplier.getInternalStatus());

        Supplier promoted = supplier.withRating(SustainabilityRating.A);
        assertEquals(SupplierInternalStatus.ACTIVE, promoted.getInternalStatus());
        assertEquals(SustainabilityRating.A, promoted.getSustainabilityRating());
    }

    @Test
    void withRating_restrictsFromActiveToOnProbation() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
        assertEquals(SupplierInternalStatus.ACTIVE, supplier.getInternalStatus());

        Supplier restricted = supplier.withRating(SustainabilityRating.D);
        assertEquals(SupplierInternalStatus.ON_PROBATION, restricted.getInternalStatus());
        assertEquals(SustainabilityRating.D, restricted.getSustainabilityRating());
    }

    @Test
    void supplier_isImmutable() {
        Supplier original = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
        Supplier modified = original.withRating(SustainabilityRating.C);

        assertNotSame(original, modified);
        assertEquals(SustainabilityRating.A, original.getSustainabilityRating());
        assertEquals(SupplierInternalStatus.ACTIVE, original.getInternalStatus());
    }

    @Test
    void getSustainabilityRatingValue_returnsStringRepresentation() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.B);
        assertEquals("B", supplier.getSustainabilityRatingValue());
    }

    @Test
    void withVersion_returnsNewInstanceWithVersion() {
        Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
        Supplier versioned = supplier.withVersion(5L);
        assertEquals(5L, versioned.getVersion());
        assertEquals(0L, supplier.getVersion());
    }
}
