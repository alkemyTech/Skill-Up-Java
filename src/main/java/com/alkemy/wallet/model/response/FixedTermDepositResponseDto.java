package com.alkemy.wallet.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FixedTermDepositResponseDto {
    private Long transactionsId;
    private Double amount;
    private Long userId;
    private Long accountId;
    private Double interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;

    private AccountResponseDto accountResponseDto;
    private UserResponseDto userResponseDto;
}
