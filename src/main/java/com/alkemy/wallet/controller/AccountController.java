package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.dto.request.AccountRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateAccountRequestDto;
import com.alkemy.wallet.model.dto.response.AccountBalanceResponseDto;
import com.alkemy.wallet.model.dto.response.AccountResponseDto;
import com.alkemy.wallet.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponseDto> geAccountBalance() {
        return ResponseEntity.status(OK).body(accountService.getAccountBalance());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(OK).body(accountService.getAccountListByUserId(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(@Validated
                                                            @RequestBody UpdateAccountRequestDto accountRequestDto,
                                                            @PathVariable("id") Long id) {
        return ResponseEntity.status(OK).body(accountService.updateAccount(id, accountRequestDto));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(@RequestParam(name = "page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(accountService.getAllAccounts(pageNumber));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createNewAccount(@Validated @RequestBody AccountRequestDto request) {
        return ResponseEntity.status(CREATED).body(accountService.createNewAccount(request));
    }
}
