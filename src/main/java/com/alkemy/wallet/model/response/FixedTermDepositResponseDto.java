package com.alkemy.wallet.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FixedTermDepositResponseDto {
    private Long id;
    private Double amount;
    private Long userId;
    private Long accountId;
    private Double interest;
    private LocalDateTime createdAt;
    private LocalDateTime closingDate;
}
