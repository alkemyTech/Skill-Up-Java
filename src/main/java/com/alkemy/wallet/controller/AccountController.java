package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.response.AccountResponseDto;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService service;

    @GetMapping("/balance")
    public ResponseEntity<List<AccountBalanceResponseDto>> getAccountBalance( @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.getAccountBalance(token), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountUserById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.getAccountUserById(userId), HttpStatus.OK);
    }
}
