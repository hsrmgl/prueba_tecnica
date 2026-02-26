package com.itxiop.tech.supplier.sandbox.domain.port.in;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
public interface CreateCandidateUseCase {
    Candidate create(String name, int duns, String country, long annualTurnover);
}
