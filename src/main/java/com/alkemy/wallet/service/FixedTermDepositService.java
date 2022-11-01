package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;

public interface FixedTermDepositService {
    Integer createFixedTermDeposit(FixedTermDepositDto fixedTermDepositDto);

}
