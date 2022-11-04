package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.BalanceResponseDTO;
import com.alkemy.wallet.service.IBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements IBalanceService {
    @Override
    public ResponseEntity<BalanceResponseDTO> getBalance() {
        return null;
    }
}
