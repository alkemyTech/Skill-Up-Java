package com.alkemy.wallet.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class BalanceDTO {
    private Double amount;
    private String currency;
}
