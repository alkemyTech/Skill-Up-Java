package com.alkemy.wallet.model.request;

import lombok.*;

import java.time.LocalDateTime;

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
