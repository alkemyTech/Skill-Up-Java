package com.alkemy.wallet.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fixed_term_deposit")
public class FixedTermDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fixed_term_deposit", nullable = false)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private Double amount;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "interest", nullable = false)
    private Double interest;

    @Column(name = "creation_date")
    @CreationTimestamp
    private Instant creationDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private Instant updateDate;
}