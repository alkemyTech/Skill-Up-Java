
package com.alkemy.wallet.dto;

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

    @Getter
    @Setter
    private Long id;

    @NotNull
    @Getter
    private Currency currency;

    @NotNull
    @Getter
    private Double transactionLimit;

    @NotNull
    @Getter
    private Double balance;

    private Date creationDate;

    private Date updateDate;

    @NotNull
    private boolean softDelete;

    private Long userId;

    public AccountDto(Currency currency) {
        this.currency = currency;
    }
}

