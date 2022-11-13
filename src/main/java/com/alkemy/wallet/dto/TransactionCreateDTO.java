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


    String description;

    @NotNull
    Integer account_id;



    public TransactionCreateDTO(Double amount, String description, Integer account_id) {
        this.amount = amount;
        this.description = description;
        this.account_id = account_id;
    }

    public TransactionCreateDTO() {
    }
}
