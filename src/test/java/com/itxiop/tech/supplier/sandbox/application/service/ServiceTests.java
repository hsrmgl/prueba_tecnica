package com.itxiop.tech.supplier.sandbox.application.service;

import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateAlreadyExistsException;
import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateCannotBeAcceptedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.CandidateNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierBannedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierCannotBeBannedException;
import com.itxiop.tech.supplier.sandbox.domain.exception.SupplierNotFoundException;
import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.model.Supplier;
import com.itxiop.tech.supplier.sandbox.domain.model.SupplierInternalStatus;
import com.itxiop.tech.supplier.sandbox.domain.model.SustainabilityRating;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CandidateRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.CountryVerifierPort;
import com.itxiop.tech.supplier.sandbox.domain.port.out.SupplierRepositoryPort;
import com.itxiop.tech.supplier.sandbox.domain.service.ScoreCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ServiceTests {

    private CandidateRepositoryPort candidateRepo;
    private SupplierRepositoryPort supplierRepo;
    private CountryVerifierPort countryVerifier;
    private DunsLockManager lockManager;

    @BeforeEach
    void setUp() {
        candidateRepo = mock(CandidateRepositoryPort.class);
        supplierRepo = mock(SupplierRepositoryPort.class);
        countryVerifier = mock(CountryVerifierPort.class);
        lockManager = new DunsLockManager();
    }

    @Nested
    class CreateCandidateTests {

        private CreateCandidateService service;

        @BeforeEach
        void setUp() {
            service = new CreateCandidateService(candidateRepo, supplierRepo, lockManager);
        }

        @Test
        void create_success() {
            when(supplierRepo.findByDuns(anyInt())).thenReturn(Optional.empty());
            when(candidateRepo.existsByDuns(anyInt())).thenReturn(false);
            when(candidateRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Candidate result = service.create("Test", 100000001, "ES", 2000000);

            assertEquals("Test", result.name());
            assertEquals(100000001, result.duns());
            verify(candidateRepo).save(any());
        }

        @Test
        void create_candidateAlreadyExists_throwsException() {
            when(supplierRepo.findByDuns(anyInt())).thenReturn(Optional.empty());
            when(candidateRepo.existsByDuns(anyInt())).thenReturn(true);

            assertThrows(CandidateAlreadyExistsException.class,
                () -> service.create("Test", 100000001, "ES", 2000000));
        }

        @Test
        void create_supplierExists_throwsException() {
            Supplier existing = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
            when(supplierRepo.findByDuns(anyInt())).thenReturn(Optional.of(existing));

            assertThrows(CandidateAlreadyExistsException.class,
                () -> service.create("Test", 100000001, "ES", 2000000));
        }

        @Test
        void create_supplierBanned_throwsException() {
            Supplier banned = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.C).ban();
            when(supplierRepo.findByDuns(anyInt())).thenReturn(Optional.of(banned));

            assertThrows(SupplierBannedException.class,
                () -> service.create("Test", 100000001, "ES", 2000000));
        }
    }

    @Nested
    class AcceptCandidateTests {

        private AcceptCandidateService service;

        @BeforeEach
        void setUp() {
            service = new AcceptCandidateService(candidateRepo, supplierRepo, countryVerifier, lockManager);
        }

        @Test
        void accept_withGoodRating_createsActiveSupplier() {
            Candidate candidate = new Candidate(100000001, "Test", "ES", 2000000);
            when(candidateRepo.findByDuns(100000001)).thenReturn(Optional.of(candidate));
            when(countryVerifier.isBanned("ES")).thenReturn(false);
            when(supplierRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            service.accept(100000001, "A");

            verify(candidateRepo).delete(100000001);
            verify(supplierRepo).save(argThat(s ->
                s.getInternalStatus() == SupplierInternalStatus.ACTIVE &&
                s.getSustainabilityRating() == SustainabilityRating.A));
        }

        @Test
        void accept_withBadRating_createsOnProbationSupplier() {
            Candidate candidate = new Candidate(100000001, "Test", "ES", 2000000);
            when(candidateRepo.findByDuns(100000001)).thenReturn(Optional.of(candidate));
            when(countryVerifier.isBanned("ES")).thenReturn(false);
            when(supplierRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            service.accept(100000001, "D");

            verify(supplierRepo).save(argThat(s ->
                s.getInternalStatus() == SupplierInternalStatus.ON_PROBATION));
        }

        @Test
        void accept_bannedCountry_throwsException() {
            Candidate candidate = new Candidate(100000001, "Test", "NK", 2000000);
            when(candidateRepo.findByDuns(100000001)).thenReturn(Optional.of(candidate));
            when(countryVerifier.isBanned("NK")).thenReturn(true);

            assertThrows(CandidateCannotBeAcceptedException.class,
                () -> service.accept(100000001, "A"));
        }

        @Test
        void accept_lowTurnover_throwsException() {
            Candidate candidate = new Candidate(100000001, "Test", "ES", 500000);
            when(candidateRepo.findByDuns(100000001)).thenReturn(Optional.of(candidate));
            when(countryVerifier.isBanned("ES")).thenReturn(false);

            assertThrows(CandidateCannotBeAcceptedException.class,
                () -> service.accept(100000001, "A"));
        }

        @Test
        void accept_candidateNotFound_throwsException() {
            when(candidateRepo.findByDuns(anyInt())).thenReturn(Optional.empty());

            assertThrows(CandidateNotFoundException.class,
                () -> service.accept(100000001, "A"));
        }
    }

    @Nested
    class BanSupplierTests {

        private BanSupplierService service;

        @BeforeEach
        void setUp() {
            service = new BanSupplierService(supplierRepo);
        }

        @Test
        void ban_onProbation_success() {
            Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.C);
            when(supplierRepo.findByDuns(100000001)).thenReturn(Optional.of(supplier));
            when(supplierRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            service.ban(100000001);

            verify(supplierRepo).save(argThat(Supplier::isDisqualified));
        }

        @Test
        void ban_active_throwsException() {
            Supplier supplier = Supplier.create(100000001, "Test", "ES", 2000000, SustainabilityRating.A);
            when(supplierRepo.findByDuns(100000001)).thenReturn(Optional.of(supplier));

            assertThrows(SupplierCannotBeBannedException.class,
                () -> service.ban(100000001));
        }

        @Test
        void ban_notFound_throwsException() {
            when(supplierRepo.findByDuns(anyInt())).thenReturn(Optional.empty());

            assertThrows(SupplierNotFoundException.class,
                () -> service.ban(100000001));
        }
    }

    @Nested
    class GetPotentialSuppliersTests {

        private GetPotentialSuppliersService service;

        @BeforeEach
        void setUp() {
            service = new GetPotentialSuppliersService(supplierRepo, new ScoreCalculator());
        }

        @Test
        void execute_filtersAndCalculatesScores() {
            List<Supplier> suppliers = List.of(
                Supplier.create(1, "S1", "ES", 2200000, SustainabilityRating.B),
                Supplier.create(2, "S2", "ES", 3000000, SustainabilityRating.C),
                Supplier.create(3, "S3", "ES", 500000, SustainabilityRating.A)
            );
            when(supplierRepo.findAllNotDisqualified()).thenReturn(suppliers);

            PotentialSuppliersResult result = service.execute(1200000, 10, 0);

            assertEquals(2, result.total());
            assertEquals(2, result.suppliers().size());
        }

        @Test
        void execute_paginationWorks() {
            List<Supplier> suppliers = List.of(
                Supplier.create(1, "S1", "ES", 2200000, SustainabilityRating.B),
                Supplier.create(2, "S2", "ES", 3000000, SustainabilityRating.C),
                Supplier.create(3, "S3", "ES", 4000000, SustainabilityRating.A)
            );
            when(supplierRepo.findAllNotDisqualified()).thenReturn(suppliers);

            PotentialSuppliersResult result = service.execute(1200000, 2, 0);

            assertEquals(3, result.total());
            assertEquals(2, result.suppliers().size());
        }
    }
}
