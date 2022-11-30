package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.constant.AccountCurrencyEnum;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "ACCOUNTS")
@SQLDelete(sql = "UPDATE accounts SET DELETED=true WHERE id=?")
@Where(clause = "DELETED=false")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "CURRENCY_TYPE")
    @Enumerated(EnumType.STRING)
    private AccountCurrencyEnum currency;

    @Column(nullable = false, name = "TRANSACTION_LIMIT")
    private Double transactionLimit;

    @Column(nullable = false, name = "BALANCE")
    private Double balance = 0.0;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CREATED_AT")
    private LocalDateTime creationDate = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;

    @Column(name = "DELETED")
    private boolean softDelete = Boolean.FALSE;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}

