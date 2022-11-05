package com.alkemy.wallet.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class AccountDTO {
    private String currency;

    private Double transactionLimit;
}
