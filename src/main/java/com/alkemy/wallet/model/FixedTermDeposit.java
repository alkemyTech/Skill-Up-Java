package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;



@Getter
@Setter
@Table(name = "FIXED_DEPOSITS")
@Entity
public class FixedTermDeposit {

    @Id
    @Column(name = "FIXED_TERM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "AMOUNT")
    private Double amount;

    @NotNull
    @Column(name = "INTEREST")
    private Double interest;

    @NotNull
    @Column(name = "CREATION_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate creationDate;

    @NotNull
    @Column(name = "CLOSING_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
}
