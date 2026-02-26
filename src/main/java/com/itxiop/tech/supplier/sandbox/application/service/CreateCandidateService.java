package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateAlreadyExistsException;
import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierBannedException;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.port.in.CreateCandidateUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CreateCandidateService implements CreateCandidateUseCase {
    private final CandidateRepositoryPort candidateRepo;
    private final SupplierRepositoryPort supplierRepo;
    private final DunsLockManager lockManager;

    public CreateCandidateService(CandidateRepositoryPort candidateRepo, SupplierRepositoryPort supplierRepo, DunsLockManager lockManager) {
        this.candidateRepo = candidateRepo;
        this.supplierRepo = supplierRepo;
        this.lockManager = lockManager;
    }

    @Override
    public Candidate create(String name, int duns, String country, long annualTurnover) {
        ReentrantLock lock = lockManager.getLock(duns);
        lock.lock();
        try {
            supplierRepo.findByDuns(duns).ifPresent(s -> {
                if (s.isDisqualified()) throw new SupplierBannedException("Supplier banned");
                throw new CandidateAlreadyExistsException("Candidate already exists");
            });
            if (candidateRepo.existsByDuns(duns)) throw new CandidateAlreadyExistsException("Candidate already exists");
            return candidateRepo.save(new Candidate(duns, name, country, annualTurnover));
        } finally {
            lock.unlock();
        }
    }
}
