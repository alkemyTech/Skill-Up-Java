package com.alkemy.wallet.model.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBalanceDto {
    private Double balanceARS;
    private Double balanceUSD;
    private Double fixedTermDeposit;

}
