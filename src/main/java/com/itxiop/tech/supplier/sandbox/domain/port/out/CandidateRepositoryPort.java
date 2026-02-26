package com.itxiop.tech.supplier.sandbox.domain.port.out;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import java.util.Optional;
public interface CandidateRepositoryPort {
    Candidate save(Candidate candidate);
    Optional<Candidate> findByDuns(int duns);
    boolean existsByDuns(int duns);
    void delete(int duns);
}
