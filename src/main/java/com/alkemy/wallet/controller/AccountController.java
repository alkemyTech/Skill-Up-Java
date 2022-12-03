package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private JwtUtil jwtUtil;
    @Autowired
    private IAccountService accountService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> getAllContinentsController(@PathVariable Long userId) throws EmptyResultDataAccessException {
        List<AccountDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> postAccount(@PathVariable Long id, @RequestHeader(name = "Authorization") String token, @RequestBody AccountDto accountDto) {
        if (jwtUtil.getKey(token) != null) {
            return null;
        } else throw new UserNotLoggedException("El usuario no está loggeado");
    }
}
