package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    public AccountDto convertToDto(Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getUser().getUserId(),
                account.getBalance(),
                account.getCurrency(),
                account.getTransactionLimit(),
                account.getCreationDate().toString(),
                account.getUpdateDate() != null ? account.getUpdateDate().toString() : "null",
                account.getSoftDelete());
    }
}
