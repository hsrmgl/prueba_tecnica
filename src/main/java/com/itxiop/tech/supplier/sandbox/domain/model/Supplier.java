package com.itxiop.tech.supplier.sandbox.domain.model;

import lombok.Value;

@Value
public class Supplier {
    private final int duns;
    private final String name;
    private final String country;
    private final long annualTurnover;
    private final SustainabilityRating sustainabilityRating;
    private final SupplierInternalStatus internalStatus;
    private final long version;

    public static Supplier create(int duns, String name, String country, long annualTurnover,
                                  SustainabilityRating rating) {
        return new Supplier(duns, name, country, annualTurnover, rating, SupplierInternalStatus.from(rating), 0L);
    }

    public String getSustainabilityRatingValue() { return sustainabilityRating.name(); }

    public boolean isDisqualified() { return internalStatus == SupplierInternalStatus.DISQUALIFIED; }
    public boolean isOnProbation() { return internalStatus == SupplierInternalStatus.ON_PROBATION; }

    public String getApiStatus() { return isDisqualified() ? "Disqualified" : "Active"; }

    public Supplier withRating(SustainabilityRating newRating) {
        SupplierInternalStatus newStatus = internalStatus.onRatingChange(newRating);
        return new Supplier(duns, name, country, annualTurnover, newRating, newStatus, version);
    }

    public Supplier ban() {
        SupplierInternalStatus newStatus = internalStatus.ban();
        return new Supplier(duns, name, country, annualTurnover, sustainabilityRating, newStatus, version);
    }

    public Supplier withVersion(long newVersion) {
        return new Supplier(duns, name, country, annualTurnover, sustainabilityRating, internalStatus, newVersion);
    }
}
