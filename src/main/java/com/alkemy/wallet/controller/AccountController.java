package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountBasicDto;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IUserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private IUserService iuserService;

  private IAccountService iAccountService;
  @Autowired
  AccountController (IAccountService iAccountService){
    this.iAccountService = iAccountService;
  }
  @GetMapping("/{userId}")
  public ResponseEntity<List<AccountDto>> getAccountById(@PathVariable("UserId") Long userId){
    List<AccountDto> listAccounts = this.iAccountService.findAllByUser(userId);
    return ResponseEntity.ok().body(listAccounts);
  }

  @GetMapping("/balance/{id}")
  public ResponseEntity<List<AccountBasicDto>> getBalance(@Valid @PathVariable Long id) {

    List<AccountBasicDto> accounts = iuserService.getAccountsBalance(id);
    return ResponseEntity.ok(accounts);
  }

  @PostMapping()
  public ResponseEntity<AccountDto> createAccount(@RequestParam String currency){
    AccountDto accountDto = this.iAccountService.createAccount(currency);

    return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
  }


  @PutMapping("/{id}")
  public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id,@RequestParam Double transactionLimitUpdated)
  {

  }

}
