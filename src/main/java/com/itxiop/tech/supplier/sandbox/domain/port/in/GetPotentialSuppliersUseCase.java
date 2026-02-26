package com.itxiop.tech.supplier.sandbox.domain.port.in;
import com.itxiop.tech.supplier.sandbox.application.service.PotentialSuppliersResult;
public interface GetPotentialSuppliersUseCase {
    PotentialSuppliersResult execute(long rate, int limit, int offset);
}
