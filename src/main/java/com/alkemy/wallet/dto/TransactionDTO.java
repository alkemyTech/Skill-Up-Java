package com.alkemy.wallet.dto;

import com.alkemy.wallet.enumeration.TypeList;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.sun.istack.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionDTO {



    Double amount;

    TypeList type;

    String description;

    Instant transactionDate;


    Integer account_id;






}
