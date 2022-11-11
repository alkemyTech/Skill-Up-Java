package com.alkemy.wallet.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalanceResponseDto {
    private Double balanceArs;
    private Double balanceUsd;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double fixedTermDeposit;

}
