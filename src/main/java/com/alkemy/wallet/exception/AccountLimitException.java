package com.alkemy.wallet.exception;

public class AccountLimitException extends RuntimeException {
    public AccountLimitException() {
        super();
    }

    public AccountLimitException(String message) {
        super(message);
    }
}
