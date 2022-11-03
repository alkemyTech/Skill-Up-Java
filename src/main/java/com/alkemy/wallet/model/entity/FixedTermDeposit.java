package com.alkemy.wallet.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name = "fixed_term_deposits")
public class FixedTermDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;
    @Column(nullable = false)
    private Double amount;
    private Long fkUserId;
    private Long fkAccountId;
    @Column(nullable = false)
    private Double interest;
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;

    @OneToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;
    @OneToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

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

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getFkAccountId() {
        return fkAccountId;
    }

    public void setFkAccountId(Long fkAccountId) {
        this.fkAccountId = fkAccountId;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
