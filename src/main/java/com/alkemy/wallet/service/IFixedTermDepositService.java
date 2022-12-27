package com.alkemy.wallet.service;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.request.FixedTermDepositSimulateRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositSimulationResponseDto;

public interface IFixedTermDepositService {

     FixedTermDepositResponseDto createNewFixedTermDeposit(FixedTermDepositRequestDto requestDto);

     FixedTermDepositSimulationResponseDto simulateDeposit(FixedTermDepositSimulateRequestDto request);
}
