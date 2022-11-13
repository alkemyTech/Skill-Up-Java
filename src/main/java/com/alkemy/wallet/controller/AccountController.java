package com.alkemy.wallet.controller;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping( "/accounts" )
public class AccountController {
    private final AccountService accountService;

    //Swagger Notation createAccount

    @Operation(summary = "Create Account of a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class)),}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "User already has an account for that currency or server couldn't complete the action")),})
    })

    //End Swagger notation

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Parameter(description = "Currency that the Account has: USD or ARS ") @RequestBody CurrencyRequestDto currency, @Parameter(description = "authentication token") @RequestHeader("Authorization") String token) {
        AccountDto account = accountService.createAccount(token, currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    //Swagger Notation getBalance
    @Operation(summary = "Get balance of a Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Balances of Accounts",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot get balance in accounts of other Users")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Balance in accounts wasn't found")),}),
    })
    //End Swagger notation

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/balance")
    public ResponseEntity<List<AccountBalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getUserBalance(token));
    }

    //Swagger Notation updateAccount
    @Operation(summary = "Updates TransactionLimit of a Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDetailDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot update others users´s accounts transaction limit ")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot update account because the account´s Id doesn't exist")),}),

    })
    //End Swagger notation

    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/{id}")
    ResponseEntity<AccountDetailDto> updateAccount(@RequestBody AccountPatchDto accountPatchDto, @Parameter(description = "id account to be updated") @PathVariable Integer id, @RequestHeader("Authorization") String userToken) throws Exception {
        return ResponseEntity.ok(accountService.updateAccount(accountPatchDto, id, userToken));

    }

    //Swagger Notation Paginated Accounts by User
    @Operation(summary = "Paginates accounts of Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedAccountsDto.class))}),
            @ApiResponse(responseCode = "400", description = "The request was not valid",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "There is missing data to enter or a data was entered incorrectly")),}),
            @ApiResponse(responseCode = "403", description = "User not authorized to perform the operation",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot paginate users´s accounts")),}),
            @ApiResponse(responseCode = "404", description = "The requested resource wasn't found or It doesn't exist",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Cannot paginate accounts because the page number or the user doesn't exist")),}),
            @ApiResponse(responseCode = "500", description = "The server encountered an unexpected condition",
                    content = {@Content(mediaType = "text/plain",
                            schema = @Schema(defaultValue = "Server couldn't paginate accounts")),})
    })
    //End Swagger notation

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping( "/{userId}" )
    public ResponseEntity<PaginatedAccountsDto> getPaginateAccountsByUserId( @PathVariable Integer userId, @Param( "page" ) Integer page, @RequestHeader("Authorization") String userToken ) {
        return ResponseEntity.ok( accountService.getPaginatedAccountsByUserId( userId, page, userToken ) );
    }

}
