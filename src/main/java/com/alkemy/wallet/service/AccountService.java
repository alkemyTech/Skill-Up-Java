package com.alkemy.wallet.service;

import com.alkemy.wallet.model.Currency;


public interface AccountService {
    void createAccount(int userId, Currency currency);
    void reduceBalance(int accountId, double amount);
}
