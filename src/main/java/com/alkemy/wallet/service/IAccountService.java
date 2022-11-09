package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountBasicDto;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IAccountService {

  AccountBasicDto findById(Long accountId);

  double calculateBalance(Long accountId);

  List<AccountDto> findAllByUser(Long userId);

  void updateBalance(Long accountId, Double amount);

  //AccountEntity findEntityById (Long accountId);

  AccountDto createAccount(String currency);

  AccountDto updateAccount(Long id,Double transactionLimitUpdated);
}
