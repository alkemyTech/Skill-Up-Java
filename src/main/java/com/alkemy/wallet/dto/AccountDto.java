
package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class AccountDto {

    private Long id;

    @NotNull
    private Currency currency;

    private Double transactionLimit;

    private Double balance;

    private boolean softDelete;

    private Long user_id;

    public AccountDto(Currency currency) {
        this.currency = currency;
    }
}

