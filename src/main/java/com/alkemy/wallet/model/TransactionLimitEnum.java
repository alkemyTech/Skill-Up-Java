package com.alkemy.wallet.model;

import lombok.Getter;

@Getter
public enum TransactionLimitEnum {
    ARS("ARS", 300000.0),
    USD("USD", 1000.0);

    private String currency;
    private double amount;

    TransactionLimitEnum(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }
}