package com.alkemy.wallet.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
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
    private LocalDateTime creationDate;
    private LocalDateTime closingDate;

    @OneToOne
    @JoinColumn(name = "fkAccountId", insertable = false, updatable = false)
    private Account account;
    @OneToOne
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;
}
