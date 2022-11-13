package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.dto.AccountDTOSlim;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.List;

//
@Component
public class AccountMapper {
    @Autowired
    ModelMapper modelMapper;

    public AccountDTO convertToAccountDTO(Account account) {
        return modelMapper.map(account, AccountDTO.class);
    }

    public Account convertToAccount(AccountDTO accountDTO) {
        return modelMapper.map(accountDTO, Account.class);
    }

    public AccountDTOSlim convertToAccountDTOSlim(Account account){
        return modelMapper.map(account, AccountDTOSlim.class);
    }

    public List<AccountDTOSlim> convertToListDTOSlim(List<Account> accounts){
        return accounts.stream().map(this::convertToAccountDTOSlim).collect(Collectors.toList());
    }

    public List<AccountDTO> accountEntityList2DTOList(List<Account> accountList){
        List<AccountDTO> dtos = new ArrayList<>();
        for(Account account: accountList){
            dtos.add(convertToAccountDTO(account));
        }
        return dtos;
    }
}