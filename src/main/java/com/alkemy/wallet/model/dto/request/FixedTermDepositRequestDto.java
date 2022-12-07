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

    @NotNull(message = "{fixed.null-amount}")
    @Min(value = 1, message = "{fixed.min-amount}")
    private Double amount;

    @NotEmpty(message = "{fixed.empty-currency}")
    @NotBlank(message = "{fixed.blank-currency}")
    private String currency;

    @NotEmpty(message = "{fixed.empty-date}")
    @NotBlank(message = "{fixed.blank-date}")
    private String closingDate;
}
