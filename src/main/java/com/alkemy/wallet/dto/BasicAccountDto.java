
package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class BasicAccountDto {

    private Long id;

    @NotNull
    private Currency currency;

//    private Double transactionLimit;

//    private Double balance;

//    private boolean softDelete;

    private Long user_id;

    public BasicAccountDto(Currency currency) {
        this.currency = currency;
    }
}

