package com.alkemy.wallet.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FixedTermDepositSimulationResponseDto {

    private LocalDate createdAt;
    private LocalDate closingDate;
    private Double amountInvested;
    private Double interestEarned;
    private Double totalEarned;
}
