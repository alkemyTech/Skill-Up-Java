package com.alkemy.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UserAlreadyHasAccountException extends RuntimeException{

    public UserAlreadyHasAccountException(String message) {
        super(message);
    }
}
