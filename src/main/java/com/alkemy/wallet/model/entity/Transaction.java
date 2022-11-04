package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.TransactionTypeEnum;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;
}
