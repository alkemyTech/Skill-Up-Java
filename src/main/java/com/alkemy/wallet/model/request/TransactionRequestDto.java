package com.alkemy.wallet.model.request;

import com.alkemy.wallet.model.TransactionTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private Long transactionsId;
    private Double amount;
    private TransactionTypeEnum type;
    private String description;
    private Long userId;
    private Long accountId;
    private LocalDateTime transactionDate;

    private UserRequestDto userRequestDto;
}
