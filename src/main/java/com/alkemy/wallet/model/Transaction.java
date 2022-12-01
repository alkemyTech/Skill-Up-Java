package com.alkemy.wallet.model;

import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name="transaction")
public class Transaction {

    @Id
    @Column(name = "TRANSACTION_ID", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "AMOUNT")
    @NotNull
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TypeOfTransaction type;

    @Column(name="DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "TRANSACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

}
