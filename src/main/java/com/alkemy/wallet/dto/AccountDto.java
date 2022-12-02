
package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class AccountDto {

    @Getter
    @Setter
    private Long id;

    @NotNull
    private Currency currency;

    @NotNull
    private Double transactionLimit;

    @NotNull
    private Double balance;

    @NotNull
    private Timestamp timestamp;

    @NotNull
    private boolean softDelete;
}

