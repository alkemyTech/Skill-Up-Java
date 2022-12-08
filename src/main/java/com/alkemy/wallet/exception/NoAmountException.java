package com.alkemy.wallet.exception;

public class NoAmountException extends RuntimeException {
    public NoAmountException() {
        super();
    }

    public NoAmountException(String message) {
        super(message);
    }
}
