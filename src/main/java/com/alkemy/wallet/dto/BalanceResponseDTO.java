package com.alkemy.wallet.dto;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class BalanceResponseDTO {
    private List<BalanceDTO> balances;
    private List<FixedTermDepositDTO> fixedTermDeposits;
}
