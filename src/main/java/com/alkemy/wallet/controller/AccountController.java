package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.CurrencyRequestDto;
import com.alkemy.wallet.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody CurrencyRequestDto currency){
        //Have to get the userId from the authentication context
        int userId = 1;
        accountService.createAccount(userId, currency.currencyRequestToEnum());
        return ResponseEntity.status(HttpStatus.CREATED).body("Successful creation of an account.");
    }
}
