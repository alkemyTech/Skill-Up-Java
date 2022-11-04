package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.BalanceResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IBalanceService {

    public ResponseEntity<BalanceResponseDTO> getBalance();
}
