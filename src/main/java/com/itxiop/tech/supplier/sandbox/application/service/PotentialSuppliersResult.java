package com.itxiop.tech.supplier.sandbox.application.service;

import java.util.List;

public record PotentialSuppliersResult(List<PotentialSupplierDto> suppliers, int total, int limit, int offset) {}
