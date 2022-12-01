package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Getter
@Setter
@Entity
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Double amount;

    @NotNull
    private Double interest;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
