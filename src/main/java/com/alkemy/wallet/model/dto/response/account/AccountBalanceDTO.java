package com.alkemy.wallet.model.dto.response.account;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDTO {

    private double balancePesos;
    private double balanceDollars;
    private double plazoFijo;

}
