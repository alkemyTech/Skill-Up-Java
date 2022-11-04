package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.AccountCurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET soft_delete=true WHERE id=?")
@Where(clause = "soft_delete=false")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountCurrencyEnum currency;

    @Column(nullable = false)
    private Double transactionLimit;

    @Column(nullable = false)
    private Double balance;

    private Long fkUserId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updateDate;

    private Boolean softDelete = Boolean.FALSE;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;

    //Getters & Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountCurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(AccountCurrencyEnum currency) {
        this.currency = currency;
    }

    public Double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(Double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(Boolean softDelete) {
        this.softDelete = softDelete;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(Long fkUserId) {
        this.fkUserId = fkUserId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<FixedTermDeposit> getFixedTermDeposits() {
        return fixedTermDeposits;
    }

    public void setFixedTermDeposits(List<FixedTermDeposit> fixedTermDeposits) {
        this.fixedTermDeposits = fixedTermDeposits;
    }
}

