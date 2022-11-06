package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.exception.FixedTermDepositException;

import java.util.List;

public interface FixedTermDepositService {
    FixedTermDepositDto createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto, String token) throws FixedTermDepositException;

    FixedTermDepositDto createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto) throws FixedTermDepositException;
    List<FixedTermDepositDto> getAccountFixedTermDeposits(int accountId);
}
