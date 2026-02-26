package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.port.in.GetCandidateUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetCandidateService implements GetCandidateUseCase {
    private final CandidateRepositoryPort candidateRepo;

    public GetCandidateService(CandidateRepositoryPort candidateRepo) { this.candidateRepo = candidateRepo; }

    @Override
    public Candidate get(int duns) {
        return candidateRepo.findByDuns(duns).orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + duns));
    }
}
