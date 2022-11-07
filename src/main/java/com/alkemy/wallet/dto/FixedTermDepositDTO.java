package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FixedTermDepositDTO {
    private Double amount;
    private LocalDate closingDate;
    private Double interests;
    private String currency;
}
