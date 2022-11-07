package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;

public interface IFixedTermDepositService {
    FixedTermDepositResponseDTO createFXD(FixedTermDepositRequestDTO requestDTO);
}
