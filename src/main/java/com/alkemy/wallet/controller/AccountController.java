package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.AccountModelAssembler;
import com.alkemy.wallet.assembler.model.AccountModel;
import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.dto.BasicAccountDto;
import com.alkemy.wallet.dto.AccountUpdateDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private AccountModelAssembler accountModelAssembler;

    @Autowired
    private PagedResourcesAssembler<AccountDto> pagedResourcesAssembler;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user's accounts",
            description = "Provides a list of the user's accounts",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BasicAccountDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nothing found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<List<BasicAccountDto>> getAllAccountByUserId(
            @Parameter(name = "User´s id",
                    required = true)
            @PathVariable Long userId) throws EmptyResultDataAccessException {
        List<BasicAccountDto> accounts = accountService.getAccountsByUserId(userId).stream()
                .map(account -> mapper.getMapper().map(account, BasicAccountDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @GetMapping
    @Operation(summary = "Paged transactions list",
            description = "Provides a paged transactions list to be verified by the user",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountModel.class))}),
            @ApiResponse(responseCode = "404", description = "There are no transactions",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<PagedModel<AccountModel>> getTransactionPage(@RequestParam(defaultValue = "0") int page) {

        Page<AccountDto> accounts = accountService.findAllAccountsPageable(page);

        PagedModel<AccountModel> model = pagedResourcesAssembler.toModel(accounts, accountModelAssembler);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping
    @Operation(summary = "Create account",
            description = "Create a new account",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BasicAccountDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> postAccount(@RequestHeader(name = "Authorization") String token,
                                         @Parameter(name = "Currency, transaction limit, balance and owner's id ",
                                                 required = true)
                                         @RequestBody BasicAccountDto basicAccountDto) {
        return accountService.postAccount(basicAccountDto, token);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update account",
            description = "Update an existent account",
            tags = "Patch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Account created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BasicAccountDto.class))}),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> updateAccountController(
            @Parameter(name = "Account's id",
                    required = true)
            @PathVariable Long id,
            @Parameter(name = "New transaction limit",
                    required = true)
            @Valid @RequestBody AccountUpdateDto newTransactionLimit,
            @RequestHeader("Authorization") String token) {
        return accountService.updateAccount(id, newTransactionLimit, token);
    }

    @GetMapping("/balance")
    @Operation(summary = "Get balance",
            description = "Provides user's accounts balances",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balances found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BalanceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Accounts not found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<List<BalanceDto>> getBalance(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(accountService.getBalance(token));
    }

}