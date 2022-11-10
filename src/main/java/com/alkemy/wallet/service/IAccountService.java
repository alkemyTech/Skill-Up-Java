package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.AccountBasicDto;

import com.alkemy.wallet.dto.CurrencyDto;
import com.alkemy.wallet.dto.PageDto;
import com.alkemy.wallet.entity.AccountEntity;
import java.util.List;

import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface IAccountService {

  AccountBasicDto findById(Long accountId);

  double calculateBalance(Long accountId);

  List<AccountDto> findAllByUser(Long userId);

  void updateBalance(Long accountId, Double amount);

  //AccountEntity findEntityById (Long accountId);

  AccountEntity createAccount(CurrencyDto currencyDto);

  AccountDto updateAccount(Long id,Double transactionLimitUpdated);

  Object addAccount(String email, CurrencyDto currencyDto);

  PageDto<AccountDto> findAllAccounts(Pageable page, HttpServletRequest request);
}
