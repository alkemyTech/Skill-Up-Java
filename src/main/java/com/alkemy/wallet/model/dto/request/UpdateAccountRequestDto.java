package com.alkemy.wallet.model.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.alkemy.wallet.utils.TransactionUtil.MIN_TRANSACTION_LIMIT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateAccountRequestDto {
    @NotNull(message = "{account.null-transaction-limit}")
    @Min(value = MIN_TRANSACTION_LIMIT, message = "{account.min-transaction-limit}")
    Double transactionLimit;
}
