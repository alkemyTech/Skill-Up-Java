package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.sun.istack.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionCreateDTO {



    Double amount;

    String type;

    String description;

    Instant transactionDate;

    @NotNull
    Account account;

    @NotNull
    User user;



}
