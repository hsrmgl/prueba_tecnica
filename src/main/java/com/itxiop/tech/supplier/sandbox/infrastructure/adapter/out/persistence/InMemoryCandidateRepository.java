package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.out.persistence;

import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCandidateRepository implements CandidateRepositoryPort {
    private final ConcurrentHashMap<Integer, Candidate> store = new ConcurrentHashMap<>();

    @Override public Candidate save(Candidate candidate) { store.put(candidate.duns(), candidate); return candidate; }
    @Override public Optional<Candidate> findByDuns(int duns) { return Optional.ofNullable(store.get(duns)); }
    @Override public boolean existsByDuns(int duns) { return store.containsKey(duns); }
    @Override public void delete(int duns) { store.remove(duns); }
}
