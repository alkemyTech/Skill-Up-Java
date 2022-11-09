package com.alkemy.wallet.model.response.list;

import com.alkemy.wallet.model.response.AccountResponseDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountListResponseDto {
    private List<AccountResponseDto> accounts;
}
