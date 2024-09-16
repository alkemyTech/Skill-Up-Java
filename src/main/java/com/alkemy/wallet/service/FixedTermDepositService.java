package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDto;
import com.alkemy.wallet.exception.FixedTermDepositException;

import java.util.List;

public interface FixedTermDepositService {
    FixedTermDepositDto createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto, String token) throws FixedTermDepositException;

    List<FixedTermDepositDto> getAccountFixedTermDeposits(int accountId);

    FixedTermDepositSimulateDto simulateFixedTermDepositDto(FixedTermDepositDto fixedTermDepositDto);
}
