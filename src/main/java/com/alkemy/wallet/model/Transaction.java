package com.alkemy.wallet.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TRANSACTIONS")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Integer transactionID;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private TransactionType type;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
    @JoinColumn( name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", nullable = false)
    private Account account;

    // TODO: Check timestamps format and auditing
    private Timestamp transactionDate;
}
