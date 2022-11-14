package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;

public interface IFixedTermDepositService {
     FixedTermDepositResponseDto create(FixedTermDepositRequestDto requestDto, String token);

}
