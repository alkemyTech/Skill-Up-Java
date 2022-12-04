package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.AccountLimitException;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ITransactionService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
public class TransactionsController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    private JwtUtil jwtUtil;

    private Mapper mapper;


    @Autowired
    private IUserService userService;

    @GetMapping("/transactions/{userId}")
    public HashSet<TransactionDto> getTransactions(@PathVariable("userId") Long id) {
        return transactionService.getByUserId(accountService.getAccountsByUserId(id));
    }

    @PatchMapping("/transactions/{id}")
    public ResponseEntity<Object> patchTransaction(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto transactionDto) {
        try {
            userService.checkLoggedUser(token);
            Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La transaccion no existe"));
            transaction.setDescription(transactionDto.getDescription());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.save(transaction), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e);
        }
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        try {
            userService.checkLoggedUser(token);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.findById(id), TransactionDto.class));
        } catch (UserNotLoggedException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }

    @PostMapping("/transactions/sendUsd")
    public ResponseEntity<Object> sendUsd(@RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }

    @PostMapping("/transactions/sendArs")
    public ResponseEntity<Object> sendArs(@RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }
}