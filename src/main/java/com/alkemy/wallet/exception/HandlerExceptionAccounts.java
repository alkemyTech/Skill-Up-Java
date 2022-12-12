package com.alkemy.wallet.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
@Hidden
@RestControllerAdvice
public class HandlerExceptionAccounts {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> applyException(EmptyResultDataAccessException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({FixedTermException.class, NotEnoughCashException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleCreateUserException(Exception exception) {
        ApiException apiException = ApiException.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .developerMessage(ExceptionUtils.getRootCauseMessage(exception))
                .build();
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseBody
    public ResponseEntity<Object> handleNotExistResourceException (Exception exception) {
        ApiException apiException = ApiException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .developerMessage(ExceptionUtils.getRootCauseMessage(exception))
                .build();
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UserNotLoggedException.class})
    @ResponseBody
    public ResponseEntity<Object> handleNotLoggedUser(Exception exception) {
        ApiException apiException = ApiException.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .developerMessage(ExceptionUtils.getRootCauseMessage(exception))
                .build();
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }
}
