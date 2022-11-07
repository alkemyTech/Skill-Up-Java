package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.TypeEnum;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long destinationAccountId;

    @NotNull(message = "Transaction type")
    @NotBlank(message = "Transaction type")
    private String type;

    @NotNull(message = "Transaction description")
    @NotBlank(message = "Transaction description")
    private String description;

    @Min(0)
    @NotNull(message = "Transaction amount")
    private Double amount;

    private String currency;

    private LocalDateTime transactionDate;
}
