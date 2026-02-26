package com.itxiop.tech.supplier.sandbox.domain.model;

public enum SupplierInternalStatus {
    ACTIVE, ON_PROBATION, DISQUALIFIED;

    public static SupplierInternalStatus from(String rating) {
        return switch (rating) {
            case "A", "B" -> ACTIVE;
            case "C", "D", "E" -> ON_PROBATION;
            default -> throw new IllegalArgumentException("Unknown rating: " + rating);
        };
    }
}
