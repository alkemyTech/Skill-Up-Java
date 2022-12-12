
package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BasicAccountDto {

    private Long id;

    @NotNull
    private Currency currency;

    private Double transactionLimit;

    private Double balance;

    private Long userId;

    public BasicAccountDto(Currency currency) {
        this.currency = currency;
    }
}

