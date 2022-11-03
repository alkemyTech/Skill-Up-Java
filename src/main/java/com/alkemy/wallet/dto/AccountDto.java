package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountDto(
        @JsonProperty("id") Integer id,
        @JsonProperty("userId") Integer userId,
        @JsonProperty("balance") Double balance,
        @JsonProperty("currency") Currency currency,
        @JsonProperty("transactionLimit") Double transactionLimit,
        @JsonProperty("creationDate") String creationDate,
        @JsonProperty("updateDate") String updateDate,
        @JsonProperty("softDelete") Boolean softDelete
) {
}

