package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountBalanceDto;
import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody CurrencyRequestDto currency, @RequestHeader("Authorization") String token) {
        accountService.createAccount(token, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful creation of an account.");
    }

    @GetMapping("/balance")
    public ResponseEntity<List<AccountBalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getUserBalance(token));
    }
}
