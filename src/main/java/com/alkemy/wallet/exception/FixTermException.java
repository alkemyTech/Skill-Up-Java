package com.alkemy.wallet.exception;

public class FixTermException extends RuntimeException{

    public FixTermException() {
        super();
    }

    public FixTermException(final String message) {
        super(message);
    }

    public FixTermException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
