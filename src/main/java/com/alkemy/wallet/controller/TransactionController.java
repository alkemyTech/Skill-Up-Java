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

import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.ARS;
import static com.alkemy.wallet.model.constant.AccountCurrencyEnum.USD;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @PostMapping("/sendArs")
    public ResponseEntity<TransactionResponseDto> sendARS(@Validated @RequestBody
                                                          TransactionRequestDto transactionRequestDto) {
        return ResponseEntity.status(CREATED)
                .body(transactionService.sendMoneyIndicatingCurrency(ARS.name(), transactionRequestDto));
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<TransactionResponseDto> sendUsd(@Validated @RequestBody
                                                          TransactionRequestDto transactionRequestDto) {
        return ResponseEntity.status(CREATED)
                .body(transactionService.sendMoneyIndicatingCurrency(USD.name(), transactionRequestDto));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(@Validated @RequestBody
                                                          TransactionRequestDto transactionRequestDto) {
        return ResponseEntity.status(CREATED).body(transactionService.doDeposit(transactionRequestDto));
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionResponseDto> payment(@Validated @RequestBody
                                                          TransactionRequestDto transactionRequestDto) {
        return ResponseEntity.status(CREATED).body(transactionService.doPayment(transactionRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable("id") Long id,
                                                                    @RequestBody
                                                                    UpdateTransactionRequestDto transactionRequestDto) {
        return ResponseEntity.status(OK).body(transactionService.updateTransaction(id, transactionRequestDto));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionDetails(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(transactionService.getTransactionDetails(id));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{userId}")
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @PathVariable(value = "userId", required = true) Long userId, @RequestParam("page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(transactionService.getAllTransactions(userId, pageNumber));
    }
}