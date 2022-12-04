package com.alkemy.wallet.exception;

import com.alkemy.wallet.service.AccountService;

public class AccountLimitException extends RuntimeException {
    public AccountLimitException() {
        super();
    }

    public AccountLimitException(String message) {
        super(message);
    }
}
