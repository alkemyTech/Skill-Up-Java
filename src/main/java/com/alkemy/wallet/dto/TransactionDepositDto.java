package com.alkemy.wallet.dto;


import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.TransactionType;
import com.alkemy.wallet.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class TransactionDepositDto {
    @JsonProperty( "id" )
    private Integer transactionID;

    @JsonProperty( "amount")
    @NotBlank( message = "Amount cannot be empty" )
    private Double amount;

    @JsonProperty( "type" )
    private TransactionType type;

    @JsonProperty( "description" )
    private String description;

    @JsonProperty( "transactionDate" )
    private Timestamp transactionDate;

    // TODO: Add account as attribute when we are able to get an Account by its id, so we can link this transaction to that account.
    public TransactionDepositDto(Double amount, String description) {
        this.amount = amount;
        this.type = TransactionType.DEPOSIT;
        this.description = description;
        this.transactionDate = new Timestamp(new Date().getTime());
    }

}


