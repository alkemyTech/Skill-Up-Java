package com.alkemy.wallet.model;

import com.alkemy.wallet.enumeration.CurrencyList;
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
@SQLDelete(sql = "UPDATE accounts SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyList currency;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}