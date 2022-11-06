package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.Account;

import java.util.List;

public interface IAccountService {
    public AccountDTO createAccount(int userId, String currency);
}