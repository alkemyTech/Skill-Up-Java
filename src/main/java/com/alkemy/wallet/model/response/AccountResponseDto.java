package com.alkemy.wallet.model.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private Long accountId;
    private String currency;
    private Double transactionLimit;
    private Double balance;
    private Long userId;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean softDelete;

    private UserResponseDto userResponseDto;
}
