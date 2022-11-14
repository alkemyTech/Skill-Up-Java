package com.alkemy.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FIXED_TERM_DEPOSITS")
@Getter
@Setter
public class FixedTermDepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;


    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    private AccountEntity accountEntity;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;

    @Column(name = "INTEREST", nullable = false)
    private Double interest;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLOSING_DATE")
    private Date closingDate;
}
