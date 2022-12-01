
package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

public class AccountDto {

    @Getter
    @Setter
    private Long id;

    @NonNull
    private Currency currency;

    @NonNull

    private Double transactionLimit;

    @NonNull
    private Double balance;

    @NonNull
    private Timestamp timestamp;

    @NonNull
    private boolean softDelete;
}

