package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.Account;

import java.util.List;

public interface IAccountService {

    List<AccountDto> getAccountsByUserId( Long userId );
}
