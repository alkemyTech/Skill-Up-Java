package com.alkemy.wallet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "transaction_limit", nullable = false)
    private Double transactionLimit;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "soft_delete")
    private boolean softDelete = Boolean.FALSE;
}