package com.alkemy.wallet.controller.exception;

import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    //validates the proper JSON format
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    //validates fields annotated with @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("errors", ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).collect(Collectors.toList()));
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    //to validate if a param annotated with @RequestParam in the endpoint is present
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(ExceptionCustom.class)
    protected ResponseEntity<Map<String, String>> badRequest(ExceptionCustom exceptionCustom){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", exceptionCustom.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Map<String, String>> badCredentialsHandler(BadCredentialsException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(UNAUTHORIZED.value()));
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Map<String, String>> accessDeniedHandler(AccessDeniedException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(UNAUTHORIZED.value()));
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Map<String, String>> entityExistHandler(EntityExistsException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(CONFLICT.value()));
        return new ResponseEntity<>(messages, CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Map<String, String>> entityNotFoundHandler(EntityNotFoundException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(NOT_FOUND.value()));
        return new ResponseEntity<>(messages, NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Map<String, String>> usernameNotFoundHandler(UsernameNotFoundException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(NOT_FOUND.value()));
        return new ResponseEntity<>(messages, NOT_FOUND);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Map<String, String>> malformedJwtHandler(MalformedJwtException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Map<String, String>> nullPointerHandler(NullPointerException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Map<String, String>> nullPointerHandler(IllegalArgumentException ex){
        Map<String, String> messages=new HashMap<>();
        messages.put("message", ex.getMessage());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("code", String.valueOf(BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }
}
