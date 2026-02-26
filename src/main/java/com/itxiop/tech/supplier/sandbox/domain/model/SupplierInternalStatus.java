package com.itxiop.tech.supplier.sandbox.domain.model;

/**
 * Finite State Machine for supplier lifecycle.
 * <p>
 * Valid transitions:
 * <ul>
 *   <li>ACTIVE  → ON_PROBATION (restrict: rating change to C/D/E)</li>
 *   <li>ON_PROBATION → ACTIVE (promote: rating change to A/B)</li>
 *   <li>ON_PROBATION → DISQUALIFIED (ban)</li>
 *   <li>DISQUALIFIED is a terminal state</li>
 * </ul>
 */
public enum SupplierInternalStatus {

    ACTIVE {
        @Override
        public boolean canBan() { return false; }

        @Override
        public boolean canUpdateRating() { return true; }
    },

    ON_PROBATION {
        @Override
        public boolean canBan() { return true; }

        @Override
        public boolean canUpdateRating() { return true; }
    },

    DISQUALIFIED {
        @Override
        public boolean canBan() { return false; }

        @Override
        public boolean canUpdateRating() { return false; }
    };

    public static SupplierInternalStatus from(SustainabilityRating rating) {
        return rating.isGoodRating() ? ACTIVE : ON_PROBATION;
    }

    public static SupplierInternalStatus from(String rating) {
        return from(SustainabilityRating.fromString(rating));
    }

    public abstract boolean canBan();

    public abstract boolean canUpdateRating();

    public SupplierInternalStatus ban() {
        if (!canBan()) {
            throw new IllegalStateException("Cannot ban supplier in state: " + this);
        }
        return DISQUALIFIED;
    }

    public SupplierInternalStatus onRatingChange(SustainabilityRating newRating) {
        if (!canUpdateRating()) {
            throw new IllegalStateException("Cannot update rating in state: " + this);
        }
        return from(newRating);
    }
}
