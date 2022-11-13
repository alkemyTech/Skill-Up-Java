package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDepositRequestDto {

    @JsonProperty( "amount")
    @NotBlank( message = "Amount cannot be empty or null" )
    private Double amount;

    @JsonProperty( "description" )
    @NotBlank( message = "description cannot be empty or null" )
    private String description;

    @JsonProperty( "accountId" )
    @NotBlank( message = "accountId cannot be empty or null" )
    private Integer accountId;

}
