package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;

public interface IAccountService {

    public AccountDTO createAccount(int userId, String currency);
}