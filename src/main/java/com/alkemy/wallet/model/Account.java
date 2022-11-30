package com.alkemy.wallet.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String currency;

    @NonNull
    @Column(name="transaction_limit", nullable = false)
    private Double transactionLimit;

    @NonNull
    private Double balance;

    //TODO: establecer relación
    private Long userId;

    @NonNull
    private Timestamp timestamp;

    @NonNull
    @Column(name="soft_delete", nullable = false)
    private boolean softDelete;

    @OneToMany(mappedBy="transaction")
    List<Transaction> transactionList;


    public Account() {

    }
}
