package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.AccountCurrencyEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountCurrencyEnum currency;
    @Column(nullable = false)
    private Double transactionLimit;
    @Column(nullable = false)
    private Double balance;
    private Long fkUserId;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean softDelete;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;
}
