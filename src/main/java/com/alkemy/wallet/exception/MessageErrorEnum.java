package com.alkemy.wallet.exception;

import lombok.Getter;

@Getter
public enum MessageErrorEnum {

    INVALID_ROLE("Invalid role"),
    USER_EXISTS("User already exists"),

    MANDATORY_PARAMETERES_MISSING("Mandatory Parameters Missing"),

    WRONG_PARAMETERS("Wrong parameters");

    private String message;

    MessageErrorEnum(String message) {
        this.message = message;
    }
}
