package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.entity.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long transactionId;

    @NotNull(message = "Transaction type")
    @NotBlank(message = "Transaction type")
    private TypeEnum type;

    @NotNull(message = "Transaction description")
    @NotBlank(message = "Transaction description")
    private String description;

    @Min(0)
    @NotNull(message = "Transaction amount")
    private Double amount;

    private LocalDateTime transactionDate;

}
