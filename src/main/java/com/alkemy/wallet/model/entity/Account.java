package com.alkemy.wallet.model.entity;

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
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted=true WHERE id=?")
@Where(clause = "deleted=false")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrencyEnum currency;

    @Column(nullable = false, name = "transaction_limit")
    private Double transactionLimit;

    @Column(nullable = false)
    private Double balance;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "created_at")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "updated_at")
    private LocalDateTime updateDate;

    @Column(name = "deleted")
    private boolean softDelete = Boolean.FALSE;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}

