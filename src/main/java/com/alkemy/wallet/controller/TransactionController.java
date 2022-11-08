package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//@Secured({"ROLE_ADMIN","ROLE_USER"})
@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
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

}
