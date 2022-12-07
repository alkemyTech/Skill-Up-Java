package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.alkemy.wallet.utils.TransactionUtil.MIN_AMOUNT_TO_SEND;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestDto {
    @Min(value = MIN_AMOUNT_TO_SEND, message = "{transaction.min-amount}")
    @NotNull(message = "{transaction.null-amount}")
    private Double amount;
    private String description;
    @NotNull(message = "{transaction.null-account}")
    private Long accountId;
}
