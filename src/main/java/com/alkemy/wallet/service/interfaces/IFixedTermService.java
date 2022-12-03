package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.dto.FixedTermDto;

public interface IFixedTermService {

    FixedTermDto createFixedTerm(FixedTermDto fixedTermDto);
}
