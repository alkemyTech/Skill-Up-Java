package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorFixedTermDep;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FixedTermDepositSimulateDTO {
    @NotNull(groups = IValidatorFixedTermDep.class)
    private Double amount;
    private LocalDate createDate;
    @NotNull(groups = IValidatorFixedTermDep.class)
    private LocalDate closingDate;
    private Double interests;
    @NotBlank(groups = IValidatorFixedTermDep.class)
    private String currency;
    private double totalAmount;
}
