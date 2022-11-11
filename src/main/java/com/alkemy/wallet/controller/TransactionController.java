package com.alkemy.wallet.controller;

import com.alkemy.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    @Autowired
    ITransactionService transactionService;

    @Autowired
    ModelMapper mapper;

    private final TransactionService service;

    @PostMapping("/sendArs")
    public ResponseEntity<String> moneySendInPesos(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double mount,
                                                   @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.moneySendInPesos(idTargetUser, mount, type, token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<String> moneySendInUsd(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double mount,
                                                 @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.moneySendInUsd(idTargetUser, mount, type, token), HttpStatus.ACCEPTED);

    }

    @PostMapping("/transactions/payment")
    public ResponseEntity<TransactionDto> saveTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = mapper.map(transactionDto, Transaction.class);

        return ResponseEntity.ok().body(mapper.map(transactionService.saveTransaction(transaction), TransactionDto.class));
    }
}
