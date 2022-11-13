package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAccountService {

    Account getByCurrencyAndUserId(String currency, Long userId);

    void editBalanceAndSave(Account account, Double newBalance);

    Account getAccountById(Long id);

    AccountResponseDto createAccount(AccountRequestDto request, String token);

    List<Account> createUserAccounts(User user);

    List<AccountBalanceResponseDto> getAccountBalance(String token);

    List<Account> getAccountsByUserId(Long userId);

    AccountResponseDto updateAccount(Long id, Double newTransactionLimit, String token);

    Page<AccountResponseDto> findAll(Integer pageNumber, Integer pageSize);
}
