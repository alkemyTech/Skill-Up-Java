package com.alkemy.wallet.model.dto.response.list;

import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionListResponseDto {
    private List<TransactionResponseDto> transactions;
}
