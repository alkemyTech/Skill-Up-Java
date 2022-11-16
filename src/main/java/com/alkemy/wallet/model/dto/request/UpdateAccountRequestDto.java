package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateAccountRequestDto {
    @NotNull(message = "The transaction limit cannot be null")
    @Min(value = 1000, message = "The limit of transaction cannot be < 1000")
    Double transactionLimit;
}
