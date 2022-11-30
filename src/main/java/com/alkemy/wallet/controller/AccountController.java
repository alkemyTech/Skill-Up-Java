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
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService service;

    @GetMapping("/balance")
    public ResponseEntity<AccountBalanceResponseDto> getBalance() {
        return ResponseEntity.status(OK).body(service.getBalance());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(OK).body(service.getListByUserId(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDto> update(@Validated @RequestBody UpdateAccountRequestDto request,
                                                     @PathVariable("id") Long id) {
        return ResponseEntity.status(OK).body(service.update(id, request));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAll(@RequestParam(name = "page") Integer pageNumber) {
        return ResponseEntity.status(OK).body(service.getAll(pageNumber));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@Validated @RequestBody AccountRequestDto request) {
        return ResponseEntity.status(CREATED).body(service.create(request));
    }
}
