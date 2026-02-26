package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper;

import com.itxiop.tech.supplier.sandbox.application.service.PotentialSupplierDto;
import com.itxiop.tech.supplier.sandbox.application.service.PotentialSuppliersResult;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PaginationResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PotentialSupplierResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PotentialSuppliersPageResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.SupplierResponse;

import java.util.List;

public interface SupplierResponseMapper {

    SupplierResponse toSupplierResponse(Supplier supplier);

    PotentialSupplierResponse toPotentialSupplierResponse(PotentialSupplierDto dto);

    List<PotentialSupplierResponse> toPotentialSupplierResponseList(List<PotentialSupplierDto> dtos);

    default PotentialSuppliersPageResponse toPageResponse(PotentialSuppliersResult result) {
        List<PotentialSupplierResponse> data = toPotentialSupplierResponseList(result.suppliers());
        PaginationResponse pagination = new PaginationResponse(result.limit(), result.offset(), result.total());
        return new PotentialSuppliersPageResponse(data, pagination);
    }
}
