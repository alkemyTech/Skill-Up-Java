package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.Currency;


import java.util.List;

public interface IAccountService {

    List<AccountDto> getAccountsByUserId(Long userId);

    AccountDto getAccountByCurrency(Long user_id, Currency currency);

    AccountDto updateAccount(Long id, AccountUpdateDto newTransactionLimit);
}
