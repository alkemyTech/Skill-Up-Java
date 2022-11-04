package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Account;

public interface IAccountService {
    public void saveAccount(Account account);

    double getBalance();
}
