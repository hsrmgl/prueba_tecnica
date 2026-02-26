package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest;

import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.domain.port.in.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper.CandidateMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CandidateController {

    private final CreateCandidateUseCase createCandidate;
    private final AcceptCandidateUseCase acceptCandidate;
    private final RefuseCandidateUseCase refuseCandidate;
    private final GetCandidateUseCase getCandidate;
    private final CandidateMapper candidateMapper;

    @PostMapping("/candidates")
    @ResponseStatus(HttpStatus.CREATED)
    public CandidateResponse create(@Valid @RequestBody CandidateRequest request) {
        Candidate c = createCandidate.create(request.name(), request.duns(), request.country(), request.annualTurnover());
        return candidateMapper.toCandidateResponse(c);
    }

    @GetMapping("/candidates/{duns}")
    public CandidateResponse get(@PathVariable int duns) {
        Candidate c = getCandidate.get(duns);
        return candidateMapper.toCandidateResponse(c);
    }

    @PostMapping("/candidates/{duns}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void accept(@PathVariable int duns, @Valid @RequestBody AcceptCandidateRequest request) {
        acceptCandidate.accept(duns, request.sustainabilityRating());
    }

    @PostMapping("/candidates/{duns}/refuse")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refuse(@PathVariable int duns) {
        refuseCandidate.refuse(duns);
    }
}
