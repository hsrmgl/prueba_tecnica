package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierCannotBeBannedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.model.SupplierInternalStatus;
import com.itxiop.tech.supplier.sandbox.domain.port.in.BanSupplierUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class BanSupplierService implements BanSupplierUseCase {
    private final SupplierRepositoryPort supplierRepo;

    public BanSupplierService(SupplierRepositoryPort supplierRepo) { this.supplierRepo = supplierRepo; }

    @Override
    public void ban(int duns) {
        Supplier supplier = supplierRepo.findByDuns(duns).orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + duns));
        if (!supplier.isOnProbation()) throw new SupplierCannotBeBannedException("Supplier cannot be banned: not on probation");
        supplierRepo.save(new Supplier(supplier.getDuns(), supplier.getName(), supplier.getCountry(),
            supplier.getAnnualTurnover(), supplier.getSustainabilityRating(), SupplierInternalStatus.DISQUALIFIED));
    }
}
