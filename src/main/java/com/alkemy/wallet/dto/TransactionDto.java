package com.alkemy.wallet.dto;

import lombok.Data;

@Data
public class TransactionDto {


    private Long id;


    private Double amount;


//    private TypeOfTransaction type;


    private String description;


    private BasicAccountDto account;

}
