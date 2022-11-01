package com.alkemy.wallet.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "FIXED_TERM_DEPOSITS")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FIXED_TERM_DEPOSIT_ID")
    private Integer fixedTermDepositId;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn( name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private Account account;

    @Column(name = "INTEREST", nullable = false)
    private Double interest;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp closingDate;
}
