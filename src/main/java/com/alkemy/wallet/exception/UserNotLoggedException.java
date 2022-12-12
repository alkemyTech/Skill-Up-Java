package com.alkemy.wallet.exception;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException() {
        super();
    }

    public UserNotLoggedException(String message) {
        super(message);
    }
}
