package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.out.persistence;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemorySupplierRepository implements SupplierRepositoryPort {

    private final ConcurrentHashMap<Integer, Supplier> store = new ConcurrentHashMap<>();
    private final AtomicLong versionCounter = new AtomicLong(0);

    @Override
    public Supplier save(Supplier supplier) {
        Supplier versioned = supplier.withVersion(versionCounter.incrementAndGet());
        store.put(versioned.getDuns(), versioned);
        return versioned;
    }

    @Override
    public Optional<Supplier> findByDuns(int duns) {
        return Optional.ofNullable(store.get(duns));
    }

    @Override
    public boolean existsByDuns(int duns) {
        return store.containsKey(duns);
    }

    @Override
    public List<Supplier> findAllNotDisqualified() {
        return store.values().stream()
            .filter(s -> !s.isDisqualified())
            .toList();
    }
}
