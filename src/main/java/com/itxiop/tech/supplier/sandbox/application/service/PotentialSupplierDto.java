package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;

public record PotentialSupplierDto(Supplier supplier, double score) {}
