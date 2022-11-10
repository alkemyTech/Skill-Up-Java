package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    //Swagger Notation createAccount
    @Operation(summary = "Create Account of a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created",
            content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = AccountDto.class)) })
    })
    //End Swagger notation

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Parameter(description = "Currency that the Account has: USD or ARS ")@RequestBody CurrencyRequestDto currency, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token) {
        AccountDto account = accountService.createAccount(token, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    //Swagger Notation getBalance
    @Operation(summary = "Get balance of a Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Balances of Accounts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDto.class)) })
    })
    //End Swagger notation

    @GetMapping("/balance")
    public ResponseEntity<List<AccountBalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getUserBalance(token));
    }

    //Swagger Notation getBalance
    @Operation(summary = "Updates TransactionLimit of a Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDetailDto.class)) })
    })
    //End Swagger notation

    @PatchMapping(value="/{id}")
    ResponseEntity<AccountDetailDto> updateAccount(@RequestBody AccountPatchDto accountPatchDto, @Parameter(description = "id account to be updated") @PathVariable Integer id, @Parameter(description = "authentication token") @RequestHeader("Authorization") String userToken) throws Exception {
        return ResponseEntity.ok(accountService.updateAccount(accountPatchDto, id, userToken));
    }
}
