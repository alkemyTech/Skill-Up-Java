package com.alkemy.wallet.exception;

public class FixedTermException extends RuntimeException{

    public FixedTermException() {
        super();
    }

    public FixedTermException(final String message) {
        super(message);
    }

    public FixedTermException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
