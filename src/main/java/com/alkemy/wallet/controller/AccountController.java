package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.response.AccountBalanceDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService service;

    @GetMapping("/balance/{idUser}")
    public ResponseEntity<List<AccountBalanceDto>> getAccountBalance(@PathVariable("idUser") Long idUser) {
        return new ResponseEntity<>(service.getAccountBalance(idUser, 0), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountUserById(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.getAccountUserById(userId), HttpStatus.OK);
    }
}
