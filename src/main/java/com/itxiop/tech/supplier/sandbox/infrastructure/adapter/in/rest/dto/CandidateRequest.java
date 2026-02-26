package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CandidateRequest(
    @NotBlank String name,
    @NotNull @Min(100000000) @Max(999999999) Integer duns,
    @NotBlank @Size(min = 2, max = 2) String country,
    @NotNull @Min(0) Long annualTurnover
) {}
