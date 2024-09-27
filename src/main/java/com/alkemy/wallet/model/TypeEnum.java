package com.alkemy.wallet.model;

import lombok.Getter;

@Getter
public enum TypeEnum {
    INCOME("Income"),
    PAYMENT("Payment"),
    DEPOSIT("Deposit");
    private final String type;
    TypeEnum(String type) {
        this.type = type;
    }
}
