package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    //TODO use the interface, not the implementation
    private final AccountServiceImpl accountBalanceService;

    //TODO use the @RequiredArgsConstructor from lombok to dependency injection and remove the constructor
    public TransactionController(AccountServiceImpl accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @GetMapping("/account/balance/{idUser}")
    public ResponseEntity<AccountBalanceDto> getAccountBalance(@PathVariable("idUser") Long idUser){
        return new ResponseEntity<>(accountBalanceService.getAccountBalance(idUser), HttpStatus.OK);

    }
}
