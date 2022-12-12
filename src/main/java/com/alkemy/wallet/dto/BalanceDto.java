package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.enums.Currency;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class BalanceDto {

    private Long id;

    private Double balance;

    private Currency currency;

    private List<FixedTermDto> fixedTerm;

}
