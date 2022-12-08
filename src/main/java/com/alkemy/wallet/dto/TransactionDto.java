package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TransactionDto {


    private Long id;


    private Double amount;


//    private TypeOfTransaction type;


    private String description;


    private AccountDto account;

}
