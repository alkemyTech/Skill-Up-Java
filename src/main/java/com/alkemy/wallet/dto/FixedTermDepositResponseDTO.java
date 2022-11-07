package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.Instant;

public class FixedTermDepositResponseDTO {
    Double amount;
    @JsonBackReference
    Account account;
    Double interest;
    Instant creationDate;
    Instant closingDate;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Instant closingDate) {
        this.closingDate = closingDate;
    }
}
