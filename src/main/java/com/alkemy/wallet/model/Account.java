package com.alkemy.wallet.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ACCOUNTS")
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "CURRENCY")
    private Currency currency;

    @Column(nullable = false, name = "TRANSACTION_LIMIT")
    private Double transactionLimit;

    @Column(nullable = false, name = "BALANCE")
    private Double balance;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;

    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;

    public Account(User user, Currency currency, Double transactionLimit, Double balance, Timestamp creationDate) {
        this.currency = currency;
        this.transactionLimit = transactionLimit;
        this.balance = balance;
        this.user = user;
        this.creationDate = creationDate;
    }
}
