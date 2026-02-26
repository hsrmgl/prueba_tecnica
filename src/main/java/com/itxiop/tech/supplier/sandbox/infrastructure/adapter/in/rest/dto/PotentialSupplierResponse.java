package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto;
public record PotentialSupplierResponse(String name, Integer duns, String country, Long annualTurnover, String status, String sustainabilityRating, Double score) {}
