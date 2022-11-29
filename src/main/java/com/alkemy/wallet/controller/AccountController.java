package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService service;

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponseDto> getAccountBalance(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.getAccountBalance(token), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserId(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(service.getAccountsByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(@Validated @RequestBody UpdateAccountRequestDto request, @PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.updateAccount(id, request, token), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<AccountResponseDto>> findAll(@RequestParam(name = "page") Integer pageNumber) {
        return  ResponseEntity.ok(service.findAll(pageNumber));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Validated @RequestBody AccountRequestDto request, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(service.createAccount(request, token), CREATED);
    }
}
