package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "fixed_deposits")
//@ApiModel("Plazos fijos")
public class FixedTermDeposit {

    @Id
    @Column(name = "fixed_term_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double amount;

    @NotNull
    private Double interest;

    @NotNull
    @CreationTimestamp
    private Date creationDate;

    @NotNull
    private Date closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
