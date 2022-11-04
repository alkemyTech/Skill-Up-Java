package com.alkemy.wallet.model.dto.response.transaction;

import com.alkemy.wallet.model.TransactionTypeEnum;
import com.alkemy.wallet.model.dto.response.account.AccountResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Long transactionsId;
    private Double amount;
    private TransactionTypeEnum type;
    private String description;
    private Long userId;
    private Long accountId;
    private LocalDateTime transactionDate;
    private UserResponseDto userResponseDto;
    private AccountResponseDto accountResponseDto;
}
