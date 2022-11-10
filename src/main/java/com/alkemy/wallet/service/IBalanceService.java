package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.BalanceDTO;
import com.alkemy.wallet.dto.BalanceResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBalanceService {
    List<BalanceDTO> getBalance();
}
