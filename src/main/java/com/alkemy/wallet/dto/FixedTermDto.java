package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FixedTermDto {

    private Long id;

    private Double amount;

    private Long accountId;

    private Double interest;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date creationDate;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date closingDate;

    private Currency currency;

}
