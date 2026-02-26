package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest;

import com.itxiop.tech.supplier.sandbox.application.service.PotentialSupplierDto;
import com.itxiop.tech.supplier.sandbox.application.service.PotentialSuppliersResult;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.port.in.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class SupplierController {
    private final GetSupplierUseCase getSupplier;
    private final BanSupplierUseCase banSupplier;
    private final GetPotentialSuppliersUseCase getPotentialSuppliers;

    public SupplierController(GetSupplierUseCase getSupplier, BanSupplierUseCase banSupplier,
                               GetPotentialSuppliersUseCase getPotentialSuppliers) {
        this.getSupplier = getSupplier;
        this.banSupplier = banSupplier;
        this.getPotentialSuppliers = getPotentialSuppliers;
    }

    @GetMapping("/suppliers/{duns}")
    public SupplierResponse get(@PathVariable int duns) {
        Supplier s = getSupplier.get(duns);
        return new SupplierResponse(s.getName(), s.getDuns(), s.getCountry(),
            s.getAnnualTurnover(), s.getApiStatus(), s.getSustainabilityRatingValue());
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
        List<PotentialSupplierResponse> data = result.suppliers().stream()
            .map(dto -> {
                Supplier s = dto.supplier();
                return new PotentialSupplierResponse(s.getName(), s.getDuns(), s.getCountry(),
                    s.getAnnualTurnover(), s.getApiStatus(), s.getSustainabilityRatingValue(), dto.score());
            }).toList();
        return new PotentialSuppliersPageResponse(data, new PaginationResponse(result.limit(), result.offset(), result.total()));
    }
}
