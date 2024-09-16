package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;

public record AccountDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("userId") Integer userId,
        @JsonProperty("balance") Double balance,
        @Getter@Setter
        @JsonProperty("currency") Currency currency,
        @JsonProperty("transactionLimit") Double transactionLimit,
        @JsonProperty("creationDate") Timestamp creationDate,
        @JsonProperty("updateDate") Timestamp updateDate,
        @JsonProperty("softDelete") Boolean softDelete
) {
}

