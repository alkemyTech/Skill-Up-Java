package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//
@Component
public class AccountMapper {
    @Autowired
    ModelMapper modelMapper;

    public AccountDTO convertToAccountDTO(Account account){
        return modelMapper.map(account, AccountDTO.class);
    }
    public Account convertToAccount(AccountDTO accountDTO){
        return modelMapper.map(accountDTO, Account.class);
    }

    public List<AccountDTO> accountEntityList2DTOList(List<Account> accountList){
        List<AccountDTO> dtos = new ArrayList<>();
        for(Account user: accountList){
            dtos.add(convertToAccountDTO(user));
        }
        return dtos;
    }
}