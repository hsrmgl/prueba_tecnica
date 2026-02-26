package com.itxiop.tech.supplier.sandbox.domain.port.out;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import java.util.List;
import java.util.Optional;
public interface SupplierRepositoryPort {
    Supplier save(Supplier supplier);
    Optional<Supplier> findByDuns(int duns);
    boolean existsByDuns(int duns);
    List<Supplier> findAllNotDisqualified();
    void updateRating(int duns, String newRating);
}
