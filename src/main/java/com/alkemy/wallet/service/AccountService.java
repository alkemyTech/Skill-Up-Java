package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;


public interface AccountService {
    AccountDto createAccount(int userId, Currency currency);
    AccountDto reduceBalance(int accountId, double amount);

    AccountDto increaseBalance(int accountId, double amount);
    AccountDto getAccountById(Integer accountId);
}
