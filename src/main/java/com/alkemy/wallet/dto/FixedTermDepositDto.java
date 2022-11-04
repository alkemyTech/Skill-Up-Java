package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter@Setter
public class FixedTermDepositDto {
    @JsonProperty("fixedTermDepositId")
    private Integer fixedTermDepositId;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("closingDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp closingDate;
}
