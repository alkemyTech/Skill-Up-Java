package com.alkemy.wallet.exception;

public class TransactionLimitExceededException extends RuntimeException{
    public TransactionLimitExceededException(String message) {
        super(message);
    }
}
