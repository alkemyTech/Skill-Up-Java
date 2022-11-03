package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
//
@Component
public class AccountMapper {
    ModelMapper modelMapper;

    public AccountDTO convertToAccountDTO(Account account){
        return modelMapper.map(account, AccountDTO.class);
    }
    public Account convertToAccount(AccountDTO accountDTO){
        return modelMapper.map(accountDTO, Account.class);
    }
}
