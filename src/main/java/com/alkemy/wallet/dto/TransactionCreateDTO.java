package com.alkemy.wallet.dto;

import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.sun.istack.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionCreateDTO {



    Double amount;

    TypeList type;

    String description;

    Instant transactionDate;

    @NotNull
    Integer account_id;



    public TransactionCreateDTO(Double amount, TypeList type, String description, Instant transactionDate, Integer account_id) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
        this.account_id = account_id;
    }

    public TransactionCreateDTO() {
    }
}
