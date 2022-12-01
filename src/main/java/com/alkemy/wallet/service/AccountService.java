package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    Mapper mapper;

    @Autowired
    IAccountRepository accountRepository;

    public List<AccountDto> getByUserId(@Valid Long user_id) {
        return accountRepository.findByUserId(user_id).stream().map(account -> mapper.getMapper().map(account, AccountDto.class)).collect(Collectors.toList());
    }
}
