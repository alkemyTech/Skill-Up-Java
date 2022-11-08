package com.alkemy.wallet.controller;


import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    //TODO use the interface, not the implementation
    private final AccountServiceImpl accountBalanceService;

    //TODO use the @RequiredArgsConstructor from lombok to dependency injection and remove the constructor
    public TransactionController(AccountServiceImpl accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByUserId(@PathVariable("userId") Long userId) {
        return new ResponseEntity(transactionService.listTransactions(userId), HttpStatus.OK);
    }

    @GetMapping("/account/balance/{idUser}")
    public ResponseEntity<AccountBalanceDto> getAccountBalance(@PathVariable("idUser") Long idUser){
        return new ResponseEntity<>(accountBalanceService.getAccountBalance(idUser), HttpStatus.OK);

    }
}
