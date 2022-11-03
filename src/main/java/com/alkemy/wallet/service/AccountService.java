package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Currency;


public interface AccountService {
    AccountDto createAccount(int userId, Currency currency);
    AccountDto reduceBalance(int accountId, double amount);
}
