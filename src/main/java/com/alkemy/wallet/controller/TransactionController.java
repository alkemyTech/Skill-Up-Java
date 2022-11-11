package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.response.AccountBalanceDto;
import com.alkemy.wallet.model.response.TransactionResponseDto;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    @Autowired
    ITransactionService transactionService;

    @Autowired
    ModelMapper mapper;

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

    @PostMapping("/transactions/payment")
    public ResponseEntity<TransactionDto> saveTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = mapper.map(transactionDto, Transaction.class);

        return ResponseEntity.ok().body(mapper.map(transactionService.saveTransaction(transaction), TransactionDto.class));
    }
}
