package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.entity.AccountEntity;
import com.alkemy.wallet.entity.UserEntity;
import com.alkemy.wallet.exception.ParamNotFound;
import com.alkemy.wallet.mapper.AccountMap;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IAccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AccountMap accountMap;


  @Override
  public List<AccountDto> findAllByUser(Long userId) {
    UserEntity entity = userRepository.findById(userId).orElseThrow(
        ()-> new ParamNotFound("User ID Invalid"));
    List<AccountEntity> accounts = accountRepository.findAllByUser(entity);
    List<AccountDto> accountsList = accountMap.accountEntityList2DtoList(accounts);

    return accountsList;
  }
}
