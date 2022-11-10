package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.entity.AccountEntity;
import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class BalanceDTO {
    private Double amount;
    private String currency;

    public BalanceDTO(AccountEntity accountEntity) {
        this.amount = accountEntity.getBalance();
        this.currency = accountEntity.getCurrency();
    }
}
