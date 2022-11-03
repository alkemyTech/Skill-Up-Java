package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    AccountRepository accountRepository;
   @Autowired
    AccountMapper accountMapper;
    //Ejemplp de uso de mapper, no corresponde a una transaccion de account
    @Override
    public AccountDTO sendUsd(AccountDTO accountDTO) {
        Account account = accountMapper.convertToAccount(accountDTO);
        Account accountNew = accountRepository.save(account);
        return accountMapper.convertToAccountDTO(accountNew);
    }


}
