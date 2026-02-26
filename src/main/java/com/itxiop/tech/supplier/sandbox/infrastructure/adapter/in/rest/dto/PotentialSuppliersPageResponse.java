package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto;
import java.util.List;
public record PotentialSuppliersPageResponse(List<PotentialSupplierResponse> data, PaginationResponse pagination) {}
