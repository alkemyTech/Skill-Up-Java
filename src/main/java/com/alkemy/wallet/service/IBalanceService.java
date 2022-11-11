package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.BalanceDTO;

import java.util.List;

public interface IBalanceService {
    List<BalanceDTO> getBalance();
}
