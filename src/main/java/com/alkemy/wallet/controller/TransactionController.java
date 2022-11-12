package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.TransactionDto;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ModelMapper mapper;

    private final ITransactionService service;

    @PostMapping("/sendArs")
    public ResponseEntity<Map<String, String>> moneySendInPesos(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double amount,
                                                                @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.sendMoney(idTargetUser, amount, "peso Argentino(ARS)", 1, type, token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Map<String, String>> moneySendInUsd(@RequestParam("idTargetUser") Long idTargetUser, @RequestParam("mount") Double amount,
                                                              @RequestParam("type") String type, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.sendMoney(idTargetUser, amount, "dolar Estadounidense(USD)", 2, type, token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/transactions/payment")
    public ResponseEntity<TransactionDto> saveTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction transaction = mapper.map(transactionDto, Transaction.class);

        return ResponseEntity.ok().body(mapper.map(service.saveTransaction(transaction), TransactionDto.class));
    }
}
