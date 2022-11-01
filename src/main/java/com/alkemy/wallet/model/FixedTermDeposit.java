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

    // TODO: Create foreign key userID
    // --- Foreign Key userID

    // TODO: Create foreign key accountID
    // --- Foreign Key accountID

    @Column(name = "INTEREST", nullable = false)
    private Double interest;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp closingDate;
}
