package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Getter;
import lombok.Setter;


public class TransactionDto {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Double amount;

    @Getter
    @Setter
    private TypeOfTransaction type;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private AccountDto account;

}
