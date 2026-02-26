package com.itxiop.tech.supplier.sandbox.domain.model;

public final class Supplier {
    private final int duns;
    private final String name;
    private final String country;
    private final long annualTurnover;
    private final SustainabilityRating sustainabilityRating;
    private final SupplierInternalStatus internalStatus;
    private final long version;

    public Supplier(int duns, String name, String country, long annualTurnover,
                    SustainabilityRating sustainabilityRating, SupplierInternalStatus internalStatus) {
        this(duns, name, country, annualTurnover, sustainabilityRating, internalStatus, 0L);
    }

    public Supplier(int duns, String name, String country, long annualTurnover,
                    SustainabilityRating sustainabilityRating, SupplierInternalStatus internalStatus, long version) {
        this.duns = duns;
        this.name = name;
        this.country = country;
        this.annualTurnover = annualTurnover;
        this.sustainabilityRating = sustainabilityRating;
        this.internalStatus = internalStatus;
        this.version = version;
    }

    public static Supplier create(int duns, String name, String country, long annualTurnover,
                                  SustainabilityRating rating) {
        return new Supplier(duns, name, country, annualTurnover, rating, SupplierInternalStatus.from(rating));
    }

    public int getDuns() { return duns; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public long getAnnualTurnover() { return annualTurnover; }
    public SustainabilityRating getSustainabilityRating() { return sustainabilityRating; }
    public String getSustainabilityRatingValue() { return sustainabilityRating.name(); }
    public SupplierInternalStatus getInternalStatus() { return internalStatus; }
    public long getVersion() { return version; }

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
