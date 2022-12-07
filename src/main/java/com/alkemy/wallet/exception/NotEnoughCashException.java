package com.alkemy.wallet.exception;

public class NotEnoughCashException extends RuntimeException {
    public NotEnoughCashException() {
        super();
    }

    public NotEnoughCashException(String message) {
        super(message);
    }
}
