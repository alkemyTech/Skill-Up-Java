package com.alkemy.wallet.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(Mistake.class)
    public ResponseEntity<Map<String, String>> badRequest(Mistake mistake){
        Map<String, String> messages=new HashMap<>();
        messages.put("Mensaje", mistake.getMessage());
        messages.put("Fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        messages.put("Código", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }
}
