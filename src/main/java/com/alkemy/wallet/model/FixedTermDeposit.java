package com.alkemy.wallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "FIXED_TERM_DEPOSITS")
@RequiredArgsConstructor
public class FixedTermDeposit {
    public FixedTermDeposit(FixedTermDeposit fix){
        this.amount=fix.getAmount();
        this.closingDate=fix.getClosingDate();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FIXED_TERM_DEPOSIT_ID")
    @Getter @Setter
    private Integer fixedTermDepositId;

    @Column(name = "AMOUNT", nullable = false)
    @Getter@Setter
    private Double amount;

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn( name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private Account account;

    @Column(name = "INTEREST", nullable = false)
    @Getter@Setter
    private Double interest;

    // TODO: check timestamps format and auditing
    @Getter@Setter
    private Timestamp creationDate;
    @Getter@Setter
    private Timestamp closingDate;
}
