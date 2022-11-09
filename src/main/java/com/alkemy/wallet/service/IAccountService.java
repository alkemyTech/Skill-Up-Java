package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;

import java.util.List;

public interface IAccountService {

    AccountResponseDto save(AccountRequestDto request);

    List<AccountBalanceDto> getAccountBalance(long idUser, int na);

    List<AccountResponseDto> getAccountUserById(long idUser);

    String sendMoney(long idUser, long idTargetUser, double amount, String money, int typeMoney, String type);
}
