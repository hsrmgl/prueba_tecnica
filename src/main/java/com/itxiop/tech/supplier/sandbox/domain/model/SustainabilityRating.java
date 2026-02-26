package com.itxiop.tech.supplier.sandbox.domain.model;

public enum SustainabilityRating {

    A(1.0),
    B(0.75),
    C(0.5),
    D(0.25),
    E(0.1);

    private final double constant;

    SustainabilityRating(double constant) {
        this.constant = constant;
    }

    public double getConstant() {
        return constant;
    }

    public boolean isGoodRating() {
        return this == A || this == B;
    }

    public static SustainabilityRating fromString(String value) {
        try {
            return SustainabilityRating.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown sustainability rating: " + value);
        }
    }
}
