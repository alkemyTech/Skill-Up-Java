package com.alkemy.wallet.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "ACCOUNTS")

@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE accounts SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")

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

    public Account(Currency currency, Double transactionLimit, Double balance, User user) {
        this.currency = currency;
        this.transactionLimit = transactionLimit;
        this.balance = balance;
        this.user = user;
        this.creationDate = new Timestamp(new Date().getTime());
        this.updateDate = new Timestamp(new Date().getTime());
        this.softDelete = false;
    }
    public Account(int accountId){
        this.accountId = accountId;
    }
}
