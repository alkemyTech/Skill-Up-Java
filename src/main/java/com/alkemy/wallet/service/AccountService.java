package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;

import java.util.List;

public interface AccountService {
    AccountDto createAccountByUserId(int userId, Currency currency);
    AccountDto createAccount(String token, CurrencyRequestDto currency);

    AccountDto reduceBalance(int accountId, double amount);
    Account findAccountByUserIdAndCurrency(User user, Currency currency);
    AccountDto increaseBalance(int accountId, double amount);
    AccountDto getAccountById(Integer accountId);
    List<AccountDto> getAccountsByUserId(int userId, String userToken);
    List<AccountBalanceDto> getUserBalance(String username);
    AccountDetailDto updateAccount(AccountPatchDto account, Integer Id, String userToken) throws Exception;
    boolean hasUserAccountById(Integer userId, Integer accountId);
    PaginatedAccountsDto getPaginatedAccountsByUserId(int userId, int page, String userToken);
}
