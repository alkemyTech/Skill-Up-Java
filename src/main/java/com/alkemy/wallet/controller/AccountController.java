package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.BalanceDTO;
import com.alkemy.wallet.dto.BalanceResponseDTO;
import com.alkemy.wallet.dto.UserRequestDTO;
import com.alkemy.wallet.model.AuthenticationRequest;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Secured({"ROLE_ADMIN","ROLE_USER"})
@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IBalanceService balanceService;
    @Autowired
    IAccountService accountService;


    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody AccountDTO account){
        return accountService.createAccount(account);
    }

    @GetMapping("/balance")
    public List<BalanceDTO> getBalance() {
        return balanceService.getBalance();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @RequestBody AccountDTO account){
        return  accountService.updateAccountId(id, account);
    }
}
