package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.port.in.GetPotentialSuppliersUseCase;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.service.ScoreCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetPotentialSuppliersService implements GetPotentialSuppliersUseCase {
    private final SupplierRepositoryPort supplierRepo;
    private final ScoreCalculator scoreCalculator;

    public GetPotentialSuppliersService(SupplierRepositoryPort supplierRepo, ScoreCalculator scoreCalculator) {
        this.supplierRepo = supplierRepo;
        this.scoreCalculator = scoreCalculator;
    }

    @Override
    public PotentialSuppliersResult execute(long rate, int limit, int offset) {
        List<Supplier> allActive = supplierRepo.findAllNotDisqualified();

        Map<String, Set<Long>> bonusByCountry = allActive.stream()
            .collect(Collectors.groupingBy(Supplier::getCountry,
                Collectors.collectingAndThen(Collectors.toList(), scoreCalculator::bonusTurnovers)));

        List<PotentialSupplierDto> eligible = allActive.stream()
            .filter(s -> s.getAnnualTurnover() > rate)
            .map(s -> new PotentialSupplierDto(s, scoreCalculator.calculate(s, bonusByCountry.getOrDefault(s.getCountry(), Set.of()))))
            .toList();

        int total = eligible.size();
        return new PotentialSuppliersResult(eligible, total, limit, offset);
    }
}
