package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AcceptCandidateRequest(
    @NotNull @Pattern(regexp = "^[A-E]$") String sustainabilityRating
) {}
