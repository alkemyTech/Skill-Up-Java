package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter@Setter
public class FixedTermDepositSimulateDto {
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("closingDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp closingDate;
    @JsonProperty("creationDate")
    private Timestamp creationDate;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("interest")
    private Double interest;
    @JsonProperty("totalAmount")
    private Double totalAmount;
}
