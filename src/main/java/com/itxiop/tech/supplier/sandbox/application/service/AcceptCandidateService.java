package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateCannotBeAcceptedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.model.SupplierInternalStatus;
import com.itxiop.tech.supplier.sandbox.domain.port.in.AcceptCandidateUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CountryVerifierPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AcceptCandidateService implements AcceptCandidateUseCase {
    private final CandidateRepositoryPort candidateRepo;
    private final SupplierRepositoryPort supplierRepo;
    private final CountryVerifierPort countryVerifier;
    private final DunsLockManager lockManager;

    public AcceptCandidateService(CandidateRepositoryPort candidateRepo, SupplierRepositoryPort supplierRepo,
                                   CountryVerifierPort countryVerifier, DunsLockManager lockManager) {
        this.candidateRepo = candidateRepo;
        this.supplierRepo = supplierRepo;
        this.countryVerifier = countryVerifier;
        this.lockManager = lockManager;
    }

    @Override
    public void accept(int duns, String sustainabilityRating) {
        Candidate candidate = candidateRepo.findByDuns(duns)
            .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + duns));
        if (countryVerifier.isBanned(candidate.country()))
            throw new CandidateCannotBeAcceptedException("Country is banned: " + candidate.country());
        final long minimumAnnualTurnover = 1_000_000L;
        if (candidate.annualTurnover() < minimumAnnualTurnover)
            throw new CandidateCannotBeAcceptedException("Annual turnover too low: " + candidate.annualTurnover());

        ReentrantLock lock = lockManager.getLock(duns);
        lock.lock();
        try {
            candidate = candidateRepo.findByDuns(duns)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + duns));
            candidateRepo.delete(duns);
            supplierRepo.save(new Supplier(
                candidate.duns(), candidate.name(), candidate.country(), candidate.annualTurnover(),
                sustainabilityRating, SupplierInternalStatus.from(sustainabilityRating)));
        } finally {
            lock.unlock();
        }
    }
}
