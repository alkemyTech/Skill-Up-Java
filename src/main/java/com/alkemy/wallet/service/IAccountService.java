package com.alkemy.wallet.service;

import com.alkemy.wallet.model.request.AccountRequestDto;
import com.alkemy.wallet.model.response.AccountBalanceDto;
import com.alkemy.wallet.model.response.AccountResponseDto;

public interface IAccountService {

    AccountResponseDto save(AccountRequestDto request);

    AccountBalanceDto getAccountBalance(long idUser);
}
