package com.alkemy.wallet.model;

import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "transactions")
//@ApiModel("Transacción")
public class Transaction {

    @Id
    @Column(name = "TRANSACTION_ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "AMOUNT")
    @NotNull
    private Double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TypeOfTransaction type;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "TRANSACTION_DATE")
    @CreationTimestamp
    private Date transactionDate;

    public Transaction(Double amount, TypeOfTransaction type, String description, Account account) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.account = account;
    }

}
