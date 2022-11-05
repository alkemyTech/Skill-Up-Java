package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.entity.AccountEntity;
import java.util.ArrayList;
import java.util.List;

public class AccountMap {


  public List<AccountDto> accountEntityList2DtoList(List<AccountEntity> accounts) {
    List<AccountDto> accountDtos = new ArrayList<>();
    AccountDto accountDto;
    for (AccountEntity account: accounts) {
      accountDto = new AccountDto();
      accountDto.setId(account.getId());
      accountDto.setCurrency(account.getCurrency());
      accountDto.setTransactionLimit(account.getTransactionLimit());
      accountDto.setBalance(account.getBalance());
      accountDto.setUpdateDate(account.getUpdateDate());
      accountDto.setCreationDate(account.getCreationDate());
      accountDtos.add(accountDto);

    }
    return accountDtos;
  }
}
