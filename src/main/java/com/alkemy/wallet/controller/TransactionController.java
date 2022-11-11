package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Object> deposit(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.saveDeposit(transactionDTO);
    }

    @PostMapping("/payment")
    public ResponseEntity<Object> payment(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.savePayment(transactionDTO);
    }

    @PostMapping("/sendArs")
    public ResponseEntity<Object> sendArs(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.sendArs(transactionDTO);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Object> sendUsd(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.sendUsd(transactionDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(@PathVariable Long userId){
        return transactionService.showAllTransactionsByUserId(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO description){
        return transactionService.updateTransaction(id, description);
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Page<TransactionEntity>> showTransactionPage(@PathVariable int pageNumber){
        return transactionService.showTransactionPage(pageNumber);
    }
}
