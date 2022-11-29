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

import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.entity.AccountCurrencyEnum.USD;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService service;

    @PostMapping("/sendArs")
    public ResponseEntity<TransactionResponseDto> sendARS(@Validated @RequestBody TransactionRequestDto request) {
        return new ResponseEntity<>(service.sendMoneyIndicatingCurrency(ARS.name(), request), OK);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionResponseDto> sendUsd(@Validated @RequestBody TransactionRequestDto request) {
        return new ResponseEntity<>(service.sendMoneyIndicatingCurrency(USD.name(), request), OK);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@Validated @RequestBody TransactionRequestDto request) {
        return new ResponseEntity<>(service.deposit(request), OK);
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDto> payment(@Validated @RequestBody TransactionRequestDto request) {
        return new ResponseEntity<>(service.payment(request), OK);
    }

    @GetMapping("/{userId}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactionsFromUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.listTransactionsByUserId(userId), OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(@PathVariable("id") Long id, @RequestBody UpdateTransactionRequestDto request) {
        return ResponseEntity.status(OK).body(service.update(id, request));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TransactionResponseDto> getDetails(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(service.getDetails(id));
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Page<TransactionResponseDto>> getAllPaged(@RequestParam("page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(service.getAll(pageNumber));
    }
}