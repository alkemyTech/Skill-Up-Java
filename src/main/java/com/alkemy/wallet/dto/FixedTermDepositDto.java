package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.FixedTermDeposit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Data
public class FixedTermDepositDto {

    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("closingDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp closingDate;
    @JsonProperty("currency")
    private Currency currency;
}
