package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FixedTermDto {

    private Long id;
    private Double amount;
    private Long accountId;
    private Double interest;
    private LocalDate creationDate;
    private LocalDate closingDate;

    private Currency currency;

}
