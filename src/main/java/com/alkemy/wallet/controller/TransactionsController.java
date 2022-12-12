package com.alkemy.wallet.controller;

import com.alkemy.wallet.assembler.TransactionModelAssembler;
import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.interfaces.IAccountService;
import com.alkemy.wallet.service.interfaces.ITransactionService;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
public class TransactionsController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAccountService accountService;

    private JwtUtil jwtUtil;

    private Mapper mapper;

    @Autowired
    private TransactionModelAssembler transactionModelAssembler;

    @Autowired
    private PagedResourcesAssembler<TransactionDto> pagedResourcesAssembler;

    @Autowired
    private IUserService userService;

    @GetMapping("/transactions/{userId}")
    @Operation(summary = "Get user's transactions",
            description = "Provides a list of the user's transactions",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nothing found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public HashSet<TransactionDto> getTransactions(
            @Parameter(name = "User´s id",
                    required = true)
            @PathVariable("userId") Long userId) {
        return transactionService.getByUserId(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/transaction/{id}")
    @Operation(summary = "Get transaction",
            description = "Provides an specific transaction",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nothing found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> getTransaction(
            @Parameter(name = "Transaction´s id",
                    required = true)
            @PathVariable Long id,
            @RequestHeader(name = "Authorization") String token) {
        return transactionService.getTransaction(id, token);
    }

    @PatchMapping("/transactions/{id}")
    @Operation(summary = "Update a transaction",
            description = "Update an existent transaction",
            tags = "Patch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nothing found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> patchTransaction(
            @Parameter(name = "Transaction´s id",
                    required = true)
            @PathVariable("id") Long id,
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "Transaction's description",
                    required = true)
            @RequestBody String description) {
        return transactionService.patchTransaction(id, token, description);
    }

    @GetMapping("/transactions/page/{id}")
    @Operation(summary = "Get user's transactions",
            description = "Provides a paged list of the user's transactions",
            tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nothing found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<PagedModel<TransactionModel>> getTransactionPage(
            @Parameter(name = "User´s id",
                    required = true)
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestHeader("Authorization") String token) {
        Page<TransactionDto> transactions = transactionService.findAllTransactionsByUserIdPageable(userId, page, token);

        PagedModel<TransactionModel> model = pagedResourcesAssembler.toModel(transactions, transactionModelAssembler);

        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/transactions/sendUsd")
    @Operation(summary = "Send USD",
            description = "Generates a transaction in USD from a logged user",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction generated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something went wrong",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<Object> sendUsd(
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "Transaction info and destined account",
                    required = true)
            @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }

    @PostMapping("/transactions/sendArs")
    @Operation(summary = "Send ARS",
            description = "Generates a transaction in ARS from a logged user",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction generated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something went wrong",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<Object> sendArs(
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "Transaction info and destined account",
                    required = true)
            @RequestBody TransactionDto destinedTransactionDto) {
        return transactionService.makeTransaction(token, destinedTransactionDto);
    }

    @PostMapping("/transactions/deposit")
    @Operation(summary = "Create deposit",
            description = "Generates a deposit",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Deposit generated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Limit exceeded",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> postDeposit(
            @Parameter(name = "Deposit info",
                    required = true)
            @RequestBody TransactionDto transactionDto) {
        return transactionService.createDeposit(transactionDto);
    }

    @PostMapping("/transactions/payment")
    @Operation(summary = "Pay",
            description = "Generates a payment",
            tags = "Post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Payment generated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "403", description = "Limit exceeded",
                    content = {@Content(mediaType = "application/json")})})
    public ResponseEntity<?> postPayment(
            @Parameter(name = "Payment info",
                    required = true)
            @RequestBody TransactionDto transactionDto) {
        return transactionService.createPayment(transactionDto);
    }
}