package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
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

    public TransactionDepositDto(Double amount, String description) {
        this.amount = amount;
        this.type = TransactionType.DEPOSIT;
        this.description = description;
        this.transactionDate = new Timestamp(new Date().getTime());
    }

}


