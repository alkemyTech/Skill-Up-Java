package com.alkemy.wallet.model.entity;

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
@Table(name = "TRANSACTIONS")
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "AMOUNT")
    private Double amount;

    @Column(nullable = false, name = "TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;

    @Column(name = "DESCRIPTION")
    private String description;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "TRANSACTION_DATE")
    private LocalDateTime transactionDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
}
