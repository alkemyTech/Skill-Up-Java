package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
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

    @Autowired
    private IUserRepository userRepository;

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
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.save(transaction), TransactionDto.class));
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        if (jwtUtil.getKey(token) != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.getMapper()
                            .map(transactionRepository.findById(id), TransactionDto.class));
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }

    @PostMapping("/transactions/sendUsd")
    public ResponseEntity<TransactionDto> sendUsd(@RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto destinedTransactionDto) {
        if (jwtUtil.getKey(token) != null) {
            Transaction transactionPayment = new Transaction();
            User senderUser = userRepository.findById(Long.parseLong(jwtUtil.getKey(token))).orElseThrow(() -> new ResourceNotFoundException("Usuario inexistente"));
            AccountDto senderAccount = accountService.getAccountByCurrency(senderUser.getId(), Currency.usd);
            if (destinedTransactionDto.getAmount() <
                    senderAccount.getBalance() && destinedTransactionDto.getAmount() < senderAccount.getTransactionLimit()) {

                Transaction transactionIncome = new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.income, destinedTransactionDto.getDescription(), destinedTransactionDto.getAccount());
                transactionPayment = new Transaction(destinedTransactionDto.getAmount(), TypeOfTransaction.payment, destinedTransactionDto.getDescription(), mapper.getMapper().map(senderAccount, Account.class));
                transactionRepository.save(transactionIncome);
                transactionRepository.save(transactionPayment);
            } else new ResponseEntity<TransactionDto>(HttpStatus.BAD_REQUEST);

            return ResponseEntity.status(HttpStatus.OK).body(mapper.getMapper().map(transactionPayment, TransactionDto.class));
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }


    @PostMapping("/transactions/sendArs")
    public ResponseEntity<TransactionDto> sendArs(@RequestHeader(name = "Authorization") String token, @RequestBody TransactionDto transactionDto) {
        return null;
    }
}