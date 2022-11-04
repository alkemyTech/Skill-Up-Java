package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.AccountCurrencyEnum;
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
@ToString
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET soft_delete=true WHERE id=?")
@Where(clause = "soft_delete=false")
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

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updateDate;

    private Boolean softDelete = Boolean.FALSE;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fkUserId", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}

