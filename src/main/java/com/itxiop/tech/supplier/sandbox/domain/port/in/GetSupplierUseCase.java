package com.itxiop.tech.supplier.sandbox.domain.port.in;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
public interface GetSupplierUseCase {
    Supplier get(int duns);
}
