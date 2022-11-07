package com.alkemy.wallet.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FixedTermDepositRequestDto {
    private Long transactionsId;
    private Double amount;
    private Long userId;
    private Long accountId;
    private Double interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;
}
