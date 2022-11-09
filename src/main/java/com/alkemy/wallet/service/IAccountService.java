package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;

import java.util.List;

public interface IAccountService {

    AccountResponseDto save(AccountRequestDto request);

    AccountRequestDto editAccountBalance(long idAccount, Double balance);

    List<AccountBalanceResponseDto> getAccountBalance(String token);

    List<AccountResponseDto> getAccountUserById(long idUser);

    String sendMoney(long idTargetUser, double amount, String money, int typeMoney, String type, String token);
}
