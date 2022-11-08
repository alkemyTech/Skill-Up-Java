package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.transaction.TransactionResponseDto;
import com.alkemy.wallet.service.ITransactionService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByUserId(@PathVariable("userId") Long userId){
        return new ResponseEntity(transactionService.listTransactions(userId), HttpStatus.OK);
    }
}
