package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import org.springframework.http.ResponseEntity;

public interface IAccountService {
    ResponseEntity<Object> createAccount(AccountDTO account);
    ResponseEntity<Object> updateAccountId(Long id, AccountDTO account);
}
