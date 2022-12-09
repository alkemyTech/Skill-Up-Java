package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
public class BalanceDto {

    private Double balance;

    private Currency currency;

    private Set<FixedTermDto> fixedTermDto;

}
