package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.TransactionTypeEnum;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    private String description;

    private Long fkUserId;

    private Long fkAccountId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;

    //Getters & Setters
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
}
