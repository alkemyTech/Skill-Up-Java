package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

public class FixedTermDepositDto {
    @Getter@Setter
    private Integer fixedTermDepositId;
    @Getter@Setter
    private Double amount;
    @Getter@Setter
    private Double interest;
    @Getter@Setter
    private Timestamp closingDate;
}
