package com.alkemy.wallet.model;

import com.alkemy.wallet.model.enums.Currency;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "accounts")
//@ApiModel("Cuenta")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Getter
    @Setter
    @Column(name = "transaction_limit", nullable = false)
    private Double transactionLimit;

    @NotNull
    @Getter
    @Setter
    @Column(name = "balance")
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "CREATION_DATE")
    @CreationTimestamp
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDate;

    @NotNull
    @Column(name = "soft_delete", nullable = false)
    private boolean softDelete;

    public Account(Currency currency) {
        this.balance = 0.;
        if (currency == Currency.ars) {
            this.transactionLimit = 300000.0;
        } else {
            this.transactionLimit = 1000.;
        }
        this.currency = currency;
    }
}
