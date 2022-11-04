package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    @Override
    public double getBalance() {
        return 0;
    }
}
