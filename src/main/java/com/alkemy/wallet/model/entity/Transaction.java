package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.TransactionTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    private String description;
    private Long fkUserId;
    private Long fkAccountId;
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;
}
