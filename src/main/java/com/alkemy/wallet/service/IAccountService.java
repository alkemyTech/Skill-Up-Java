package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.Account;

public interface IAccountService {
    public AccountDTO sendUsd(AccountDTO accountsDTO);
}
