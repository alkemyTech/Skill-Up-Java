package com.alkemy.wallet.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    @NotNull
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeOfTransaction type;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "timestamp")
    private Timestamp timestamp;

}
