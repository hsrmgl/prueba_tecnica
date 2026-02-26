package com.itxiop.tech.supplier.sandbox.domain.model;

public final class Supplier {
    private final int duns;
    private final String name;
    private final String country;
    private final long annualTurnover;
    private volatile String sustainabilityRating;
    private volatile SupplierInternalStatus internalStatus;

    public Supplier(int duns, String name, String country, long annualTurnover, String sustainabilityRating, SupplierInternalStatus internalStatus) {
        this.duns = duns;
        this.name = name;
        this.country = country;
        this.annualTurnover = annualTurnover;
        this.sustainabilityRating = sustainabilityRating;
        this.internalStatus = internalStatus;
    }

    public int getDuns() { return duns; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public long getAnnualTurnover() { return annualTurnover; }
    public String getSustainabilityRating() { return sustainabilityRating; }
    public SupplierInternalStatus getInternalStatus() { return internalStatus; }

    public boolean isDisqualified() { return internalStatus == SupplierInternalStatus.DISQUALIFIED; }
    public boolean isOnProbation() { return internalStatus == SupplierInternalStatus.ON_PROBATION; }

    public String getApiStatus() { return isDisqualified() ? "Disqualified" : "Active"; }

    public Supplier withRating(String newRating) {
        return new Supplier(duns, name, country, annualTurnover, newRating, SupplierInternalStatus.from(newRating));
    }
}
