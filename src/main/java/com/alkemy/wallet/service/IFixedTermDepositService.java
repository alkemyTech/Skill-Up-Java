package com.alkemy.wallet.service;

import com.alkemy.wallet.model.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.response.FixedTermDepositResponseDto;

public interface IFixedTermDepositService {
     FixedTermDepositResponseDto save(FixedTermDepositRequestDto requestDto, String token);

}
