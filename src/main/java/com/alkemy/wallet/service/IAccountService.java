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

    Account getByCurrencyAndUserId(String currency, Long userId);

    void editBalanceAndSave(Account account, Double newBalance);

    Account getById(Long id);

    AccountResponseDto create(AccountRequestDto request);

    List<Account> createDefaultAccounts(User user);

    AccountBalanceResponseDto getBalance();

    List<AccountResponseDto> getListByUserId(Long userId);

    AccountResponseDto update(Long id, UpdateAccountRequestDto request);

    Page<AccountResponseDto> getAll(Integer pageNumber);
}
