package com.alkemy.wallet.model;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table( name = "ACCOUNTS" )
public class Account {

    @Id
    @GeneratedValue( strategy = IDENTITY )
    @Column( name = "ACCOUNT_ID" )
    private Integer accountId;

    @Column( nullable = false, name = "CURRENCY" )
    private String currency;

    @Column( nullable = false, name = "TRANSACTION_LIMIT" )
    private Double transactionLimit;

    @Column( nullable = false, name = "BALANCE" )
    private Double balance;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;
}
