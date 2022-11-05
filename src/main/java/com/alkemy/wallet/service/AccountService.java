package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;

import java.util.List;

public interface AccountService {
    AccountDto createAccountByUserId(int userId, Currency currency);
    AccountDto createAccountByUsername(String username, Currency currency);

    AccountDto reduceBalance(int accountId, double amount);

    AccountDto increaseBalance(int accountId, double amount);
    AccountDto getAccountById(Integer accountId);

    List<AccountDto> getAccountsByUserId(int userId);

    AccountDto getAccountByUserAndCurrency(int userId, CurrencyRequestDto currencyRequestDto);


}
