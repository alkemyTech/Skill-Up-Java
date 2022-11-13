package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.AccountPageDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.Account;

import java.util.List;
import java.util.Map;

public interface IAccountService {
    public AccountDTO createAccount(int userId, String currency);
    AccountDTO createAccountWithToken(String token, String currency);
    List<AccountDTO> getAccountsByUser(Integer id);

    Map<String, Object> getAccounts();

    AccountPageDTO getAccountsByPage(Integer page);
}