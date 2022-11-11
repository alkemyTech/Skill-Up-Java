package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.FixedTermDepositEntity;
import lombok.*;

import java.util.Set;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class BalanceDTO {
    private Double amount;
    private String currency;
    private Set<FixedTermDepositEntity> fixedTermDeposit;

    public BalanceDTO(AccountEntity accountEntity) {
        this.amount = accountEntity.getBalance();
        this.currency = accountEntity.getCurrency();
        this.fixedTermDeposit = accountEntity.getFixedTermsDeposit();
    }
}
