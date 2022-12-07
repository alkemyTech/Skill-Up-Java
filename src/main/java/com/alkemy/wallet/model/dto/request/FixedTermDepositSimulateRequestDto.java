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

    @NotNull(message = "{fixed.null-amount}")
    @Min(value = MIN_TO_INVEST, message = "{fixed.min-amount}")
    private Double amount;

    @NotEmpty(message = "{fixed.empty-date}")
    @NotBlank(message = "{fixed.blank-date}")
    private String closingDate;
}
