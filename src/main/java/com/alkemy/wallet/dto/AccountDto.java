package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountDto {

    private Long id;

    private Currency currency;

    private Double transactionLimit;

    private Double balance;

    private boolean softDelete;

    private Long user_id;

}