package com.alkemy.wallet.exception;

import com.alkemy.wallet.dto.AccountDto;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException() {
        super();
    }

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
