package com.alkemy.wallet.model.response.list;

import com.alkemy.wallet.model.response.FixedTermDepositResponseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FixedTermDepositListResponseDto {
    private List<FixedTermDepositResponseDto> fixedTermDeposits;
}
