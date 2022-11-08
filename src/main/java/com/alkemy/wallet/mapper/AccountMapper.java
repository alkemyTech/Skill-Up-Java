package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Account;
import lombok.RequiredArgsConstructor;

import com.alkemy.wallet.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    public AccountDto convertToDto(Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getUser().getUserId(),
                account.getBalance(),
                account.getCurrency(),
                account.getTransactionLimit(),
                account.getCreationDate(),
                account.getUpdateDate() != null ? account.getUpdateDate() : new Timestamp(new Date().getTime()),
                account.getSoftDelete());
    }

    public Account convertToEntity(AccountDto accountDto){
        return new Account(
                accountDto.id(),
                accountDto.currency(),
                accountDto.transactionLimit(),
                accountDto.balance(),
                userService.getUserById(accountDto.userId()),
                accountDto.creationDate(),
                accountDto.updateDate(),
                accountDto.softDelete()
        );
    }

    public AccountBalanceDto convertAccountDtoToAccountBalanceDto(AccountDto accountDto){
        return new AccountBalanceDto(
                accountDto.id(),
                accountDto.balance(),
                accountDto.currency()
        );
    }
    public AccountDetailDto convertToAccountDetailDto(Account account){
        return mapper.map( account, AccountDetailDto.class);
    }

}
