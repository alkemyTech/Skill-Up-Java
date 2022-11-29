package com.alkemy.wallet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FixedTermDepositSimulateRequestDto {

    @NotNull(message = "Amount to invest cannot be null")
    @Min(value = 1, message = "The minimum to invest is 1")
    private Double amount;

    @NotEmpty(message = "Closing date cannot be null or empty")
    @NotBlank(message = "Closing date cannot be whitespaces")
    private String closingDate;
}
