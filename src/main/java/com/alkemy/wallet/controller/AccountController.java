package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private IAccountService accountService;
    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(
        @RequestParam(required = false) int userId,
        @RequestParam(required = false) String currency) throws Exception{

        AccountDTO accountDTO = accountService.createAccount(userId,currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<AccountDTO>> getAccountsByUser(@PathVariable Integer id){
        List<AccountDTO> accounts = accountService.getAccountsByUser(id);
        return ResponseEntity.ok().body(accounts);
    }
}