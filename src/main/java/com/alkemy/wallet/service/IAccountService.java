package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface IAccountService {

    Account getAccountById(long IdAccount);
    AccountResponseDto save(Account entity);
    List<AccountResponseDto> createUserAccounts(Long userId);

    AccountResponseDto editAccountBalance(long idAccount, Double newBalance);

    List<AccountBalanceResponseDto> getAccountBalance(String token);

    List<AccountResponseDto> getAccountUserById(long idUser);

    String sendMoney(long idTargetUser, double amount, String money, int typeMoney, String type, String token);

    AccountResponseDto updateAccount(Long accountId, Double newTransactionLimit, String token);
}
