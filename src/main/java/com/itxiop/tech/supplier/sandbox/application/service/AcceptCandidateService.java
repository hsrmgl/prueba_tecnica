package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateCannotBeAcceptedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.model.SustainabilityRating;
import com.itxiop.tech.supplier.sandbox.domain.port.in.AcceptCandidateUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CountryVerifierPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class AcceptCandidateService implements AcceptCandidateUseCase {

    private static final long MINIMUM_ANNUAL_TURNOVER = 1_000_000L;

    private final CandidateRepositoryPort candidateRepo;
    private final SupplierRepositoryPort supplierRepo;
    private final CountryVerifierPort countryVerifier;
    private final DunsLockManager lockManager;

    @Override
    public void accept(int duns, String sustainabilityRating) {
        SustainabilityRating rating = SustainabilityRating.fromString(sustainabilityRating);

        Candidate candidate = candidateRepo.findByDuns(duns)
            .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + duns));
        if (countryVerifier.isBanned(candidate.country()))
            throw new CandidateCannotBeAcceptedException("Country is banned: " + candidate.country());
        if (candidate.annualTurnover() < MINIMUM_ANNUAL_TURNOVER)
            throw new CandidateCannotBeAcceptedException("Annual turnover too low: " + candidate.annualTurnover());

        ReentrantLock lock = lockManager.getLock(duns);
        lock.lock();
        try {
            candidate = candidateRepo.findByDuns(duns)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + duns));
            candidateRepo.delete(duns);
            supplierRepo.save(Supplier.create(
                candidate.duns(), candidate.name(), candidate.country(),
                candidate.annualTurnover(), rating));
        } finally {
            lock.unlock();
        }
    }
}
