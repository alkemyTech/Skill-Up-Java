package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.account.AccountBalanceDTO;
import com.alkemy.wallet.service.AccountBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final AccountBalanceService accountBalanceService;

    public TransactionController(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @GetMapping("/account/balance/{idUser}")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance(@PathVariable("idUser") Long idUser){
        return new ResponseEntity<>(accountBalanceService.getAccountBalance(idUser), HttpStatus.OK);

    }
}
