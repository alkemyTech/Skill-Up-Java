package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.ResourceNotFoundException;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.interfaces.IAccountService;
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
    private TransactionService transactionService;

    private IAccountService accountService;

    private JwtUtil jwtUtil;

    private Mapper mapper;

    @GetMapping("/transactions/{userId}")
    public HashSet<TransactionDto> getTransactions(@PathVariable("userId") Long id) {

        return transactionService.getByUserId(accountService.getAccountsByUserId(id));
    }

    @PatchMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> patchTransaction(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto transactionDto) {
        String userId = jwtUtil.getKey(token);
        if (userId != null) {
            Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La transaccion no existe"));
            transaction.setDescription(transactionDto.getDescription());
            transactionDto = mapper.getMapper().map(transactionRepository.save(transaction), TransactionDto.class);
            return ResponseEntity.status(HttpStatus.OK).body(transactionDto);
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        if (jwtUtil.getKey(token) != null) {
            TransactionDto transactionDto = mapper.getMapper().map(transactionRepository.findById(id), TransactionDto.class);
            return ResponseEntity.status(HttpStatus.OK).body(transactionDto);
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }
}