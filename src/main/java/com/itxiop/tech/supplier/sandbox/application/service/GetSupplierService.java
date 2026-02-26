package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.port.in.GetSupplierUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierService implements GetSupplierUseCase {
    private final SupplierRepositoryPort supplierRepo;

    public GetSupplierService(SupplierRepositoryPort supplierRepo) { this.supplierRepo = supplierRepo; }

    @Override
    public Supplier get(int duns) {
        return supplierRepo.findByDuns(duns).orElseThrow(() -> new SupplierNotFoundException("Supplier not found: " + duns));
    }
}
