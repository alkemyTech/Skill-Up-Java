package com.alkemy.wallet.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class HandlerExceptionAccounts {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> applyException(EmptyResultDataAccessException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
