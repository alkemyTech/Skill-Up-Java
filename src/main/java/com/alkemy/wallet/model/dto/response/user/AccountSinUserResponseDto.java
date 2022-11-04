package com.alkemy.wallet.model.dto.response.user;

import com.alkemy.wallet.model.dto.response.account.AccountResponseDto;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSinUserResponseDto {
    private List<AccountResponseDto> accountResponseDto;

    public List<AccountResponseDto> getAccountResponseDto() {
        return accountResponseDto;
    }

    public void setAccountResponseDto(List<AccountResponseDto> accountResponseDto) {
        this.accountResponseDto = accountResponseDto;
    }
}
