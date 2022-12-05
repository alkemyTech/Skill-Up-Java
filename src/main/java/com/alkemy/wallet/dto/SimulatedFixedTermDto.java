package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SimulatedFixedTermDto {

    private Double amount;

    private Double interest;

    private Double totalAmount;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate creationDate;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate closingDate;

}
