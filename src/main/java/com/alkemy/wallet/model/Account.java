package com.alkemy.wallet.model;

import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "account")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "currency")
    private Currency currency;

    @NotNull
    @Getter
    @Setter
    @Column(name = "transaction_limit", nullable = false)
    private Double transactionLimit;

    @NotNull
    @Getter
    @Setter
    @Column(name = "balance")
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

//    @Column(name="user_id", nullable = false)
//    private Long userId;

    @NotNull
    private Timestamp timestamp;

    @NotNull
    @Column(name = "soft_delete", nullable = false)
    private boolean softDelete;

    public Account() {

    }
}
