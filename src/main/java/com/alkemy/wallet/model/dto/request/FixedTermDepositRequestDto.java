package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.alkemy.wallet.utils.FixedTermDepositUtil.MIN_TO_INVEST;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FixedTermDepositRequestDto {

    @NotNull(message = "{fixed.invalid-amount}")
    @Min(value = MIN_TO_INVEST, message = "{fixed.invalid-amount}")
    private Double amount;

    @NotEmpty(message = "{fixed.invalid-currency}")
    @NotBlank(message = "{fixed.invalid-currency}")
    private String currency;

    @NotEmpty(message = "{fixed.invalid-date}")
    @NotBlank(message = "{fixed.invalid-date}")
    private String closingDate;
}
