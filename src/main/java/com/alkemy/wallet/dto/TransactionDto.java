package com.alkemy.wallet.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {

    private Long id;

    private Double amount;

//    private TypeOfTransaction type;

    private Date transactionDate;

    private String description;


    private AccountDto account;

    private BasicAccountDto account;


}
