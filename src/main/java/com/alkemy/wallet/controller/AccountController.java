package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody CurrencyRequestDto currency, @RequestHeader("Authorization") String token) {
        AccountDto account = accountService.createAccount(token, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/balance")
    public ResponseEntity<List<AccountBalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getUserBalance(token));
    }

    @PatchMapping(value="/{id}")
    ResponseEntity<AccountDetailDto> updateAccount(@RequestBody AccountPatchDto accountPatchDto, @PathVariable Integer id, @RequestHeader("Authorization") String userToken) throws Exception {
        return ResponseEntity.ok(accountService.updateAccount(accountPatchDto, id, userToken));
    }
}
