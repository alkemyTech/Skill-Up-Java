package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.model.TransactionEntity;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

    @GetMapping("/transactionspage")
    public ResponseEntity<Page<TransactionEntity>> showTransactionPage(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size, Model model){
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        transactionService.addNavigationAttributesToModel(pageNumber,model,pageRequest);

        return transactionService.showTransactionPage(pageRequest);
    }
}