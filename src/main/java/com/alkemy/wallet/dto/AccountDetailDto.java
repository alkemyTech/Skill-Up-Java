package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto {

    @JsonProperty("id") Integer id;

    @JsonProperty("userId") Integer userId;

    @JsonProperty("balance") Double balance;

    @JsonProperty("currency") Currency currency;

    @JsonProperty("transactionLimit") Double transactionLimit;

    @JsonProperty("creationDate") Timestamp creationDate;

    @JsonProperty("updateDate") Timestamp updateDate;
}
