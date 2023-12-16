package com.alkemy.wallet.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.alkemy.wallet.utils.FixedTermDepositUtil.MIN_TO_INVEST;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FixedTermDepositSimulateRequestDto {

    @NotNull(message = "{fixed.invalid-amount}")
    @Min(value = MIN_TO_INVEST, message = "{fixed.invalid-amount}")
    private Double amount;

    @NotEmpty(message = "{fixed.invalid-date}")
    @NotBlank(message = "{fixed.invalid-date}")
    private String closingDate;
}
