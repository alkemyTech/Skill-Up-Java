package com.alkemy.wallet.controller.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    //validates the proper JSON format
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable
    (HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", BAD_REQUEST.value());
        messages.put("status", BAD_REQUEST.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    //validates fields annotated with @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
    (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", BAD_REQUEST.value());
        messages.put("status", BAD_REQUEST.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList()));
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    //to validate if a param annotated with @RequestParam in the endpoint is present
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter
    (MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", BAD_REQUEST.value());
        messages.put("status", BAD_REQUEST.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    protected ResponseEntity<Map<String, Object>> handleSignatureException(SignatureException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", UNAUTHORIZED.value());
        messages.put("status", UNAUTHORIZED.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Map<String, Object>> handleExpiredJwt(ExpiredJwtException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", UNAUTHORIZED.value());
        messages.put("status", UNAUTHORIZED.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", UNAUTHORIZED.value());
        messages.put("status", UNAUTHORIZED.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", FORBIDDEN.value());
        messages.put("status", FORBIDDEN.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, FORBIDDEN);
    }

    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", UNAUTHORIZED.value());
        messages.put("status", UNAUTHORIZED.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Map<String, Object>> handleMalformedJwt(MalformedJwtException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", BAD_REQUEST.value());
        messages.put("status", BAD_REQUEST.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Map<String, Object>> handleEntityExists(EntityExistsException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", CONFLICT.value());
        messages.put("status", CONFLICT.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, CONFLICT);
    }

    @ExceptionHandler(InputMismatchException.class)
    protected ResponseEntity<Map<String, Object>> handleInputMismatch(InputMismatchException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", CONFLICT.value());
        messages.put("status", CONFLICT.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", NOT_FOUND.value());
        messages.put("status", NOT_FOUND.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", NOT_FOUND.value());
        messages.put("status", NOT_FOUND.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", CONFLICT.value());
        messages.put("status", CONFLICT.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, CONFLICT);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", NOT_FOUND.value());
        messages.put("status", NOT_FOUND.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, NOT_FOUND);
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<Map<String, Object>> handleDateTimeParse(DateTimeParseException ex) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("code", BAD_REQUEST.value());
        messages.put("status", BAD_REQUEST.toString());
        messages.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));
        messages.put("message", ex.getMessage());
        return new ResponseEntity<>(messages, BAD_REQUEST);
    }
}
