package com.alkemy.wallet.model.dto.response;

import com.alkemy.wallet.model.TransactionTypeEnum;
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

    public Long getTransactionsId() {
        return transactionsId;
    }

    public void setTransactionsId(Long transactionsId) {
        this.transactionsId = transactionsId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public UserResponseDto getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(UserResponseDto userResponseDto) {
        this.userResponseDto = userResponseDto;
    }

    public AccountResponseDto getAccountResponseDto() {
        return accountResponseDto;
    }

    public void setAccountResponseDto(AccountResponseDto accountResponseDto) {
        this.accountResponseDto = accountResponseDto;
    }
}
