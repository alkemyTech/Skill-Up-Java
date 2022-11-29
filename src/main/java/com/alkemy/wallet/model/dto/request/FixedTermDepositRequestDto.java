package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FixedTermDepositRequestDto {

    @NotNull(message = "Must declare the amount of money to deposit")
    @Min(value = 1, message = "The amount must be 1 positive minimum")
    private Double amount;

    @NotEmpty(message = "Specify the currency is mandatory")
    @NotBlank(message = "Currency cannot be whitespaces")
    private String currency;

    @NotEmpty(message = "Must declare the closing date")
    @NotBlank(message = "Closing date cannot be whitespaces")
    private String closingDate;
}
