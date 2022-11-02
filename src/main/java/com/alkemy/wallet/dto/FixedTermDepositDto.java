package com.alkemy.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

public class FixedTermDepositDto {
    @Getter@Setter
    @JsonProperty("fixedTermDepositId")
    private Integer fixedTermDepositId;
    @Getter@Setter
    @JsonProperty("amount")
    private Double amount;
    @Getter@Setter
    @JsonProperty("closingDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp closingDate;
}
