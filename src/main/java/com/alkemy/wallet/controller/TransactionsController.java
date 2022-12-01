package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.interfaces.IAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;

@RestController
public class TransactionsController {

    private TransactionService transactionService;

    private IAccountService accountService;

    @GetMapping("/transactions/{userId}")
    public HashSet<TransactionDto> getTransactions(@PathVariable("userId") Long id) {

        return transactionService.getByUserId(accountService.getAccountsByUserId(id));
    }
}
