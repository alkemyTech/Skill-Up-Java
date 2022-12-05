package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SimulatedFixedTermDto {

    private Double amount;

    private Double interest;

    private Double totalAmount;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date creationDate;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date closingDate;

}
