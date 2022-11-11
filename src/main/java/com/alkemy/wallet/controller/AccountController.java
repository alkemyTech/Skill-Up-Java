package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.BalanceResponseDTO;
import com.alkemy.wallet.model.entity.AccountEntity;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@Secured({"ROLE_ADMIN","ROLE_USER"})
@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
@RestController
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
    public ResponseEntity<BalanceResponseDTO> getBalance() {
        return balanceService.getBalance();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable Long id, @RequestBody AccountDTO account){
        return  accountService.updateAccountId(id, account);
    }

    @GetMapping("/accountspage")
    public ResponseEntity<Page<AccountEntity>> showAccountsPage(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size, Model model){
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        accountService.addNavigationAttributesToModel(pageNumber,model,pageRequest);

        return accountService.showAccountsPage(pageRequest);
    }
}
