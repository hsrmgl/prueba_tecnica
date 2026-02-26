package com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest;

import com.itxiop.tech.supplier.sandbox.domain.exception.*;
import com.itxiop.tech.supplier.sandbox.infrastructure.adapter.in.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CandidateAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCandidateAlreadyExists(CandidateAlreadyExistsException e) { return new ErrorResponse("Candidate already exists"); }

    @ExceptionHandler(SupplierBannedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSupplierBanned(SupplierBannedException e) { return new ErrorResponse("Supplier banned"); }

    @ExceptionHandler(CandidateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCandidateNotFound(CandidateNotFoundException e) { return new ErrorResponse(e.getMessage()); }

    @ExceptionHandler(CandidateCannotBeAcceptedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCandidateCannotBeAccepted(CandidateCannotBeAcceptedException e) { return new ErrorResponse(e.getMessage()); }

    @ExceptionHandler(SupplierNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSupplierNotFound(SupplierNotFoundException e) { return new ErrorResponse(e.getMessage()); }

    @ExceptionHandler(SupplierCannotBeBannedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSupplierCannotBeBanned(SupplierCannotBeBannedException e) { return new ErrorResponse(e.getMessage()); }
}
