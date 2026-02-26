package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.mapper;

import com.itxiop.tech.supplier.sandbox.domain.model.Candidate;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.CandidateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateResponse toCandidateResponse(Candidate candidate);
}
