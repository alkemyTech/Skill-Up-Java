package com.alkemy.wallet.dto;

import lombok.*;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class BalanceTotalDTO {
    private Double depositAmount;
    private Double paymentAmount;
    private Double incomeAmount;
}
