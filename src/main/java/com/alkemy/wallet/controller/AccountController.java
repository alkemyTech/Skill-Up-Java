package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("accounts")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> getAllContinentsController(@PathVariable Long userId) throws EmptyResultDataAccessException {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }
}
