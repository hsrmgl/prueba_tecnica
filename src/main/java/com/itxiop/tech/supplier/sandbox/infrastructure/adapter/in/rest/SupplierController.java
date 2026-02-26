package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest;

import com.itxiop.tech.supplier.sandbox.application.service.PotentialSuppliersResult;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.port.in.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper.SupplierResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SupplierController {
    private final GetSupplierUseCase getSupplier;
    private final BanSupplierUseCase banSupplier;
    private final GetPotentialSuppliersUseCase getPotentialSuppliers;
    private final SupplierResponseMapper supplierMapper;

    @GetMapping("/suppliers/{duns}")
    public SupplierResponse get(@PathVariable int duns) {
        Supplier s = getSupplier.get(duns);
        return supplierMapper.toSupplierResponse(s);
    }

    @PostMapping("/suppliers/{duns}/ban")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ban(@PathVariable int duns) {
        banSupplier.ban(duns);
    }

    @GetMapping("/suppliers/potential")
    public PotentialSuppliersPageResponse getPotential(
            @RequestParam long rate,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        PotentialSuppliersResult result = getPotentialSuppliers.execute(rate, limit, offset);
        return supplierMapper.toPageResponse(result);
    }
}
