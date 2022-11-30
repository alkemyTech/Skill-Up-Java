package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.USD;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService service;

    @PostMapping("/sendArs")
    public ResponseEntity<TransactionResponseDto> sendARS(@Validated @RequestBody TransactionRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.sendMoneyIndicatingCurrency(ARS.name(), request));
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionResponseDto> sendUsd(@Validated @RequestBody TransactionRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.sendMoneyIndicatingCurrency(USD.name(), request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@Validated @RequestBody TransactionRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.deposit(request));
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDto> payment(@Validated @RequestBody TransactionRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.payment(request));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsFromUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(OK).body(service.listTransactionsByUserId(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(@PathVariable("id") Long id,
                                                         @RequestBody UpdateTransactionRequestDto request) {
        return ResponseEntity.status(OK).body(service.update(id, request));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TransactionResponseDto> getDetails(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(service.getDetails(id));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllPaged(@RequestParam("page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(service.getAll(pageNumber));
    }
}