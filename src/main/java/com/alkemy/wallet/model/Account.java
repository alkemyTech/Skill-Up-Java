package com.alkemy.wallet.model;

import com.alkemy.wallet.model.enums.Currency;
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
    private Currency currency;

    @NonNull
    @Column(name="transaction_limit", nullable = false)
    private Double transactionLimit;

    @NonNull
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @NonNull
    private Timestamp timestamp;

    @NonNull
    @Column(name="soft_delete", nullable = false)
    private boolean softDelete;
//
//    @OneToMany(mappedBy="transaction")
//    List<Transaction> transactionList;
//

    public Account() {

    }
}
