package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.service.IAccountService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  private IAccountService iAccountService;
  AccountController (IAccountService iAccountService){
    this.iAccountService = iAccountService;
  }
  @GetMapping("/{userId}")
  public ResponseEntity<List<AccountDto>> getAccountById(@PathVariable("UserId") Long userId){
    List<AccountDto> listAccounts = this.iAccountService.findAllByUser(userId);
    return ResponseEntity.ok().body(listAccounts);
  }

}
