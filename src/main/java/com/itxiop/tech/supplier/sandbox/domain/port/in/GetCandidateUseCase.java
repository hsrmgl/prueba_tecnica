package com.itxiop.tech.supplier.sandbox.domain.port.in;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
public interface GetCandidateUseCase {
    Candidate get(int duns);
}
