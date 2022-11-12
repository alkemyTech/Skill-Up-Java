package com.alkemy.wallet.dto;

import com.alkemy.wallet.dto.validator.IValidatorAccount;
import com.alkemy.wallet.dto.validator.IValidatorDeposit;
import com.alkemy.wallet.dto.validator.IValidatorPayment;
import com.alkemy.wallet.dto.validator.IValidatorSendArsUsd;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long destinationAccountId;

    @NotBlank(groups= {IValidatorDeposit.class, IValidatorPayment.class, IValidatorAccount.class})
    private String type;
    @NotBlank(groups= {IValidatorDeposit.class, IValidatorPayment.class, IValidatorAccount.class})
    private String description;
    @NotNull(groups= {IValidatorDeposit.class, IValidatorPayment.class, IValidatorAccount.class,IValidatorSendArsUsd.class})
    private Double amount;
    @NotBlank(groups= {IValidatorDeposit.class, IValidatorPayment.class, IValidatorAccount.class})
    private String currency;
    private LocalDateTime transactionDate;
}
