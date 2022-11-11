package com.alkemy.wallet.model.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDto {
    private String currency;
    private Double transactionLimit;
    private Double balance;
    private Long userId;
}
