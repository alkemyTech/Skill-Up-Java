package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.BalanceResponseDTO;
import com.alkemy.wallet.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IBalanceService balanceService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance() {
        return balanceService.getBalance();
    }
}
