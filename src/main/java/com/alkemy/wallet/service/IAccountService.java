package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.AccountDto;
import java.util.List;
public interface IAccountService {

  List<AccountDto> findAllByUser(Long userId);
}
