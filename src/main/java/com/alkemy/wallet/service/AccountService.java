package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.repository.IAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final IAccountRepository accountRepository;

    public AccountService(IAccountRepository accountRepository){
        this.accountRepository= accountRepository;
    }
    public Optional<Account> findById(Long accountId){
        return accountRepository.findById(accountId);
    }
    public boolean save(Account account){
        if(accountRepository.save(account)!=null){
            return true;
        } else {
            return false;
        }
    }
}
