package com.alkemy.wallet.model;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table( name = "ACCOUNT" )
public class Account {

    @Id
    @GeneratedValue( strategy = IDENTITY )
    private Integer accountId;

    @Column( nullable = false )
    private String currency;

    @Column( nullable = false )
    private Double transactionLimit;

    @Column( nullable = false )
    private Double balance;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;
}
