package com.alkemy.wallet.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class HandlerExceptionUser {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public Map<String, String> applyException(SQLIntegrityConstraintViolationException e){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Email already in use", e.getMessage());
        return errorMap;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, String> applyException(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }


    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> applyException(NullPointerException e){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Invalid email", e.getMessage());
        return errorMap;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, String> applyException(AccessDeniedException e){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Resource out of permissions", e.getMessage());
        return errorMap;
    }
}
