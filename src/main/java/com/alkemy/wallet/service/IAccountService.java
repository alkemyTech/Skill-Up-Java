package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;

public interface IAccountService {

    AccountResponseDto save(AccountRequestDto request);

    AccountBalanceDto getAccountBalance(long idUser);
}
