package com.alkemy.wallet.exception;

import lombok.Getter;

@Getter
public enum MessageErrorEnum {

    INVALID_ROLE("Invalid role"),
    USER_EXISTS("User already exists");

    private String message;

    MessageErrorEnum(String message) {
        this.message = message;
    }
}
