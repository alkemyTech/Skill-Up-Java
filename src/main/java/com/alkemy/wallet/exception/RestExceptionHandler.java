package com.alkemy.wallet.exception;

import com.alkemy.wallet.dto.ApiErrorDTO;
import com.alkemy.wallet.enumeration.ErrorList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Throwable.class)
    protected ResponseEntity<Object> handleThrowable(Throwable ex, WebRequest req){
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                Arrays.asList("")
        );
        return handleExceptionInternal((Exception) ex, apiErrorDTO, new HttpHeaders(), apiErrorDTO.getStatus(),req);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = TransactionException.class)
    protected ResponseEntity<Object> handleTransactionException(RuntimeException ex, WebRequest request) {
        ApiErrorDTO errorDTO = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                Arrays.asList(ErrorList.REQUEST_FAILED.getMessage())
        );
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = FixedTermException.class)
    protected ResponseEntity<Object> handleFixedTermException(RuntimeException ex, WebRequest request) {
        ApiErrorDTO errorDTO = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                Arrays.asList(ErrorList.REQUEST_FAILED.getMessage())
        );
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> notFoundException(RuntimeException ex, WebRequest request) {
        ApiErrorDTO errorDTO = new ApiErrorDTO(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                Arrays.asList(ErrorList.OBJECT_NOT_FOUND.getMessage())
        );
        return handleExceptionInternal(ex, errorDTO, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


}
