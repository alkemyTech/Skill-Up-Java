package com.alkemy.wallet.model;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.TransactionTypeEnum;
import com.alkemy.wallet.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {
    private Long id;
    private Double amount;
    private TransactionTypeEnum type;
    private String description;
    private LocalDateTime transactionDate;
    private User user;
    private Account account;
}
