package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class AccountController {
    @Autowired
    AccountServiceImpl accountServiceImpl;

    @PostMapping("sendUsd/{idAccount}/")

    public ResponseEntity<AccountDTO> sendUsd(@RequestBody AccountDTO accountdto){
        return null;
    }
}
