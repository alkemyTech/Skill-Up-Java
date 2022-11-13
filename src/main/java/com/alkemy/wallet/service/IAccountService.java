package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.response.AccountResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IAccountService {

    Account getAccountById(long idAccount);
    AccountResponseDto createAccount(AccountResponseDto dto);

    List<Account> createUserAccounts(User user);

    AccountResponseDto editAccountBalance(long idAccount, Double newBalance);

    List<AccountBalanceResponseDto> getAccountBalance(String token);

    List<AccountResponseDto> getAccountUserById(long idUser);

    AccountResponseDto updateAccount(long accountId, double newTransactionLimit, String token);

    Optional<Account> findTopByUserId(Long userId);
    void save(Account account);

    Page<AccountResponseDto> findAll(Integer pageNumber, Integer pageSize);
}
