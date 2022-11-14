package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDto {
    @Min(value = 1, message = "The amount of money must be a minimum of 1 positive")
    @NotNull(message = "Cannot be a null amount of money")
    private Double amount;
    private String description;
    @NotNull(message = "Must declares the account that send/receive the money")
    private Long accountId;
}
