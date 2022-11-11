package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDTO;
import org.springframework.http.ResponseEntity;

public interface IFixedTermDepositService {
     ResponseEntity<Object> saveFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit);

   FixedTermDepositSimulateDTO simulateDeposit(FixedTermDepositDTO fixedTermDeposit);
}
