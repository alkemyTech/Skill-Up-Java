package com.alkemy.wallet.model;

import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.TypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private Long transactionId;

    @Column(name = "amount")
    @NotNull
    private Double amount;

    @Column(name = "type", nullable = false, length = 8)
    @Enumerated (value = EnumType.STRING)
    private TypeEnum type;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AccountEntity account;

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime transactionDate;

}
