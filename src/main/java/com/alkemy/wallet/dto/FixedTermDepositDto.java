package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedTermDepositDto {

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("closingDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp closingDate;
    @JsonProperty("currency")
    private Currency currency;
}
