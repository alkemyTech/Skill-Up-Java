package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.NumberFormat;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE user_id=?")
@Where(clause = "deleted=false")
public class AccountEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Long accountId;

    @Column(name = "currency")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull
    private String currency;

    @Column(name = "transactionLimit")
    @NotNull
    private Double transactionLimit;

    @Column(name = "balance")
    @NotNull
    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserEntity user;

    @Column(name = "creationDate")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "updateDate")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy="account")
    private Set<TransactionEntity> transactions;

    @OneToMany(mappedBy="account", cascade = CascadeType.ALL)
    private Set<FixedTermDepositEntity> fixedTermsDeposit;
}
