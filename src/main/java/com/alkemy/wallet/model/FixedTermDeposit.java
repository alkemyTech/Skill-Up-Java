package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "fixed_deposits")
@ApiModel("Plazos fijos")
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
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date creationDate;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
