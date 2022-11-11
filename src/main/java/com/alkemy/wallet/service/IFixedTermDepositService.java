package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IFixedTermDepositService {
     ResponseEntity<Object> saveFixedTermDeposit(FixedTermDepositDTO fixedTermDeposit);

   FixedTermDepositSimulateDTO simulateDeposit(FixedTermDepositDTO fixedTermDeposit);
}
