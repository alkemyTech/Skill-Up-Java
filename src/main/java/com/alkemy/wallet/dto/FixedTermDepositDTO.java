package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorFixedTermDep;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class FixedTermDepositDTO {
    @NotBlank(groups = IValidatorFixedTermDep.class)
    private Double amount;
    private LocalDate closingDate;
    @NotBlank(groups = IValidatorFixedTermDep.class)
    private Double interests;
    private String currency;
}
