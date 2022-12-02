
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
    @Getter
    private Currency currency;

    @NotNull
    @Getter
    private Double transactionLimit;

    @NotNull
    @Getter
    private Double balance;

    @NotNull
    private Timestamp timestamp;

    @NotNull
    private boolean softDelete;
}

