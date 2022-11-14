package com.alkemy.wallet.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "FIXED_TERM_DEPOSITS")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "AMOUNT")
    private Double amount;

    @Column(nullable = false, name = "INTEREST")
    private Double interest;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CREATED_AT")
    private LocalDateTime creationDate = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CLOSING_DATE")
    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;
}
