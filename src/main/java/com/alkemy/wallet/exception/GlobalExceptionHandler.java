package com.alkemy.wallet.exception;

import com.alkemy.wallet.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BankException.class})
    public ResponseEntity<MessageDTO> bankExceptionHandler(BankException ex, WebRequest request) {
        MessageDTO message = MessageDTO.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
