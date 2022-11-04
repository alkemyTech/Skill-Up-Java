package com.alkemy.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code= HttpStatus.BAD_REQUEST)
public class TransactionException extends RuntimeException{
    public TransactionException(String error){super(error);}
}
