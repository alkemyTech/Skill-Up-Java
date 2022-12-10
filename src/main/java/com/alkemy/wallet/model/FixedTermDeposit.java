package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@Entity
@Table(name = "fixed_deposits")
@ApiModel("Plazos fijos")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
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
    private LocalDate creationDate;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
