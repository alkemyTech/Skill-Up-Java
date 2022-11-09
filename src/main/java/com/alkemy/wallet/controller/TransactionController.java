package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.entity.TransactionEntity;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(@PathVariable Long userId){
        return transactionService.showAllTransactionsByUserId(userId);
    }
}
