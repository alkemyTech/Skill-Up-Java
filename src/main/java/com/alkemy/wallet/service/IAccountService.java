package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccountService {

    AccountResponseDto create(AccountRequestDto request);

    List<Account> createDefaultAccounts(User user);

    AccountResponseDto update(Long id, UpdateAccountRequestDto request);

    void editBalanceAndSave(Account account, Double newBalance);

    AccountBalanceResponseDto getBalance();

    Account getByCurrencyAndUserId(String currency, Long userId);

    Account getById(Long id);

    List<AccountResponseDto> getListByUserId(Long userId);

    Page<AccountResponseDto> getAll(Integer pageNumber);
}
