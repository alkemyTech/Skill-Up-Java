package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorAccount;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class AccountDTO {
    @NotBlank(groups= {IValidatorAccount.class})
    private String currency;
    @NotNull(groups= {IValidatorAccount.class})
    private Double transactionLimit;
}
