package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
