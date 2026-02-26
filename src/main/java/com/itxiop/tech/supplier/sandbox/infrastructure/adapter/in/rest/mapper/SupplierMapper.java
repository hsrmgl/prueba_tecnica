package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper;

import com.itxiop.tech.supplier.sandbox.application.service.PotentialSupplierDto;
import com.itxiop.tech.supplier.sandbox.application.service.PotentialSuppliersResult;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PaginationResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PotentialSupplierResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.PotentialSuppliersPageResponse;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.SupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(source = "apiStatus", target = "status")
    @Mapping(source = "sustainabilityRatingValue", target = "sustainabilityRating")
    SupplierResponse toSupplierResponse(Supplier supplier);

    @Mapping(source = "supplier.name", target = "name")
    @Mapping(source = "supplier.duns", target = "duns")
    @Mapping(source = "supplier.country", target = "country")
    @Mapping(source = "supplier.annualTurnover", target = "annualTurnover")
    @Mapping(source = "supplier.apiStatus", target = "status")
    @Mapping(source = "supplier.sustainabilityRatingValue", target = "sustainabilityRating")
    PotentialSupplierResponse toPotentialSupplierResponse(PotentialSupplierDto dto);

    List<PotentialSupplierResponse> toPotentialSupplierResponseList(List<PotentialSupplierDto> dtos);

    default PotentialSuppliersPageResponse toPageResponse(PotentialSuppliersResult result) {
        List<PotentialSupplierResponse> data = toPotentialSupplierResponseList(result.suppliers());
        PaginationResponse pagination = new PaginationResponse(result.limit(), result.offset(), result.total());
        return new PotentialSuppliersPageResponse(data, pagination);
    }
}
