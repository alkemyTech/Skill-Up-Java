package com.alkemy.wallet.service;

import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.alkemy.wallet.model.Account} entity
 */
@Data
public class AccountDto implements Serializable {
    private final Long id;
    private final Currency currency;
    private final Double transactionLimit;
    private final Double balance;
    private final Long userId;
}