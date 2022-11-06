package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface IAccountService {
    public ResponseEntity<Object> createAccount(AccountDTO account);


}
