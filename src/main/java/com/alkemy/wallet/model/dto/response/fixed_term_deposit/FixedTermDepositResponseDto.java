package com.alkemy.wallet.model.dto.response.fixed_term_deposit;

import com.alkemy.wallet.model.dto.response.account.AccountResponseDto;
import com.alkemy.wallet.model.dto.response.user.UserResponseDto;
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

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public AccountResponseDto getAccountResponseDto() {
        return accountResponseDto;
    }

    public void setAccountResponseDto(AccountResponseDto accountResponseDto) {
        this.accountResponseDto = accountResponseDto;
    }

    public UserResponseDto getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(UserResponseDto userResponseDto) {
        this.userResponseDto = userResponseDto;
    }
}
