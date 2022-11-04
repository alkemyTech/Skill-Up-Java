package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;


public interface AccountService {
    AccountDto createAccount(int userId, Currency currency);
    AccountDto reduceBalance(int accountId, double amount);
    Account findAccountByUserIdAndCurrency(User user, Currency currency);
}
