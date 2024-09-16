package com.alkemy.wallet.exception;

public class InvalidAccountCurrencyException extends RuntimeException {
    public InvalidAccountCurrencyException(String message) {
        super(message);
    }
}
