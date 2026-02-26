package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.port.in.RefuseCandidateUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefuseCandidateService implements RefuseCandidateUseCase {
    private final CandidateRepositoryPort candidateRepo;

    @Override
    public void refuse(int duns) {
        if (!candidateRepo.existsByDuns(duns)) throw new CandidateNotFoundException("Candidate not found: " + duns);
        candidateRepo.delete(duns);
    }
}
