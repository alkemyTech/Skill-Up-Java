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
@Table(name = "fixed_term_deposits")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;

    @Column(nullable = false)
    private Double amount;

    private Long fkUserId;

    private Long fkAccountId;

    @Column(nullable = false)
    private Double interest;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime closingDate;

    @ManyToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;
}
