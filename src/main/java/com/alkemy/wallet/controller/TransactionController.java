package com.alkemy.wallet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.service.TransactionService;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> getAllByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = service.findAllByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
