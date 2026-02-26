package com.itxiop.tech.supplier.sandbox.domain.port.out;
public interface CountryVerifierPort {
    boolean isBanned(String country);
}
