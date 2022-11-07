package com.alkemy.wallet.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(String message) {
        super(message);
    }
}
