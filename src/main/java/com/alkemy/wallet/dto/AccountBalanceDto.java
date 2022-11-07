package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AccountBalanceDto {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("fixedTermDeposits")
    private List<FixedTermDepositDto> fixedTermDeposits;
}
