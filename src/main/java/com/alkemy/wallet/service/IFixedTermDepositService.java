package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import org.springframework.http.ResponseEntity;

public interface IFixedTermDepositService {
    public ResponseEntity<Object> saveFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit);
}
