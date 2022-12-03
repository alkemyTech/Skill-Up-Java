package com.alkemy.wallet.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class AccountUpdateDto {

    @NotNull
    private Double transactionLimit;
}

