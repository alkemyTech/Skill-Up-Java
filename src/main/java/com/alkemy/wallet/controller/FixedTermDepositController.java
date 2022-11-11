package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.FixedTermDepositDTO;
import com.alkemy.wallet.dto.FixedTermDepositSimulateDTO;
import com.alkemy.wallet.service.IFixedTermDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FixedTermDepositController {
    @Autowired
    IFixedTermDepositService fixedTermDepositService;

    @PostMapping("/fixedDeposit")
    public ResponseEntity<Object> postFixedDeposit(@RequestBody FixedTermDepositDTO fixedTermDeposit) {
        return fixedTermDepositService.saveFixedTermDeposit(fixedTermDeposit);
    }
    @GetMapping("/fixedTermDeposit/simulate")
    public  FixedTermDepositSimulateDTO simulateDeposit(@RequestBody FixedTermDepositDTO fixedTermDeposit){
        return fixedTermDepositService.simulateDeposit(fixedTermDeposit);
    }
}
