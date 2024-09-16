package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDto {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("balance")
    private Double balance;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("fixedTermDeposits")
    private List<FixedTermDepositDto> fixedTermDeposits;

    public AccountBalanceDto(int id, Double balance, Currency currency){
        this.id = id;
        this.balance = balance;
        this.currency = currency;
    }
}
