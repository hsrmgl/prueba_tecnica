package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper;

import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.CandidateResponse;

public interface CandidateResponseMapper {

    CandidateResponse toCandidateResponse(Candidate candidate);
}
